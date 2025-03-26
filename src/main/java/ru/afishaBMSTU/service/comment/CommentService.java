package ru.afishaBMSTU.service.comment;

import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.comment.NewCommentDto;
import ru.afishaBMSTU.dto.comment.UpdateCommentRequest;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentDto createComment(NewCommentDto newCommentDto, UUID userExternalId, Long eventId);
    CommentDto updateComment(UpdateCommentRequest updateCommentRequest, UUID userExternalId, Long commentId);
    void deleteComment(UUID userExternalId, Long commentId);
    List<CommentDto> getCommentsByOwner(UUID userExternalId, Integer from, Integer size);
}
