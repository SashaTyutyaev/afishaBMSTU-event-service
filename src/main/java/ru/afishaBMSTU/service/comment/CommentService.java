package ru.afishaBMSTU.service.comment;

import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.comment.NewCommentDto;
import ru.afishaBMSTU.dto.comment.UpdateCommentRequest;

import java.util.List;

public interface CommentService {
    CommentDto createComment(NewCommentDto newCommentDto, Long userId, Long eventId);
    CommentDto updateComment(UpdateCommentRequest updateCommentRequest, Long userId, Long commentId);
    void deleteComment(Long userId, Long commentId);
    List<CommentDto> getCommentsByOwner(Long userId, Integer from, Integer size);
}
