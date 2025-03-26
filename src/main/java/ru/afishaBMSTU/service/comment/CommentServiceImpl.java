package ru.afishaBMSTU.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.comment.NewCommentDto;
import ru.afishaBMSTU.dto.comment.UpdateCommentRequest;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.CommentMapper;
import ru.afishaBMSTU.model.comment.Comment;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.repository.CommentRepository;
import ru.afishaBMSTU.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto createComment(NewCommentDto newCommentDto, UUID externalId, Long eventId) {
        Event event = getEventById(eventId);
        Comment comment = commentMapper.toComment(newCommentDto);
        comment.setAuthorExternalId(externalId);
        comment.setEvent(event);
        comment.setCreatedAt(LocalDateTime.now());
        log.info("Create comment {} successful", comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateComment(UpdateCommentRequest updateCommentRequest, UUID externalId, Long commentId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthorExternalId().equals(externalId)) {
            log.error("User must be owner of comment {}", commentId);
            throw new DataIntegrityViolationException("User is not owner of comment " + commentId);
        }
        comment.setText(updateCommentRequest.getText());
        log.info("Updating comment {} successful", comment.getId());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(UUID externalId, Long commentId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthorExternalId().equals(externalId)) {
            log.error("User must be owner of comment {}", commentId);
            throw new DataIntegrityViolationException("User is not owner of comment " + commentId);
        }
        commentRepository.delete(comment);
        log.info("Delete comment {} successful", comment.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByOwner(UUID externalId, Integer from, Integer size) {
        Pageable pageable = validatePageable(from, size);
        return commentRepository.getCommentsByAuthorExternalId(externalId, pageable).stream()
                .map(commentMapper::toCommentDto).collect(Collectors.toList());
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("Comment with id {} not found", commentId);
            return new NotFoundException("Comment with id " + commentId + " not found");
        });
    }

    private PageRequest validatePageable(Integer from, Integer size) {
        if (from == null || from < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }
        if (size == null || size < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }

        return PageRequest.of(from / size, size);
    }
}
