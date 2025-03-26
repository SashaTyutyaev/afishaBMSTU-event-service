package ru.afishaBMSTU.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.event.EventFilterDto;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.CommentMapper;
import ru.afishaBMSTU.mapper.EventMapper;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.model.event.State;
import ru.afishaBMSTU.model.views.Views;
import ru.afishaBMSTU.repository.CommentRepository;
import ru.afishaBMSTU.repository.EventRepository;
import ru.afishaBMSTU.repository.ViewsRepository;
import ru.afishaBMSTU.specification.EventSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository repository;
    private final ViewsRepository viewsRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(EventFilterDto filters, String sort, int from, int size) {
        if (sort == null) {
            sort = "VIEWS";
        }

        if (filters.getRangeEnd() != null) {
            if (LocalDateTime.parse(filters.getRangeEnd(), FORMATTER)
                    .isBefore(LocalDateTime.parse(filters.getRangeStart(), FORMATTER))) {
                log.error("End time must be after start time");
                throw new IncorrectParameterException("End time must be after start time");
            }
        }

        Pageable pageable = validatePageable(from, size);
        Specification<Event> spec = EventSpecification.withFilters(filters, sort);
        List<Event> events = repository.findAll(spec, pageable).getContent();

        return events.stream()
                .map(eventMapper::toEventFullDto)
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto getEvent(Long id, String ip) {
        Event event = getEventById(id);

        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Event {} is not published", id);
            throw new NotFoundException("Event " + id + " is not published");
        }

        if (viewsRepository.findAllByEventIdAndIp(id, ip).isEmpty()) {
            event.setViews(event.getViews() + 1);
            repository.save(event);
            Views views = Views.builder()
                    .ip(ip)
                    .event(event)
                    .viewedAt(LocalDateTime.now())
                    .build();
            viewsRepository.save(views);
        }

        log.info("Get event {} successful", event.getId());
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByEvent(Long eventId, Integer from, Integer size) {
        Pageable pageable = validatePageable(from, size);
        Event event = getEventById(eventId);
        return commentRepository.getCommentsByEvent(event, pageable).stream()
                .map(commentMapper::toCommentDto).collect(Collectors.toList());
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

    private Event getEventById(Long eventId) {
        return repository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }
}
