package ru.afishaBMSTU.controller.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.comment.NewCommentDto;
import ru.afishaBMSTU.dto.comment.UpdateCommentRequest;
import ru.afishaBMSTU.model.user.CustomUserDetails;
import ru.afishaBMSTU.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/users/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newCommentDto,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @RequestParam Long eventId) {
        return commentService.createComment(newCommentDto, customUserDetails.getId(), eventId);
    }

    @PatchMapping("{commentId}")
    public CommentDto updateComment(@RequestBody UpdateCommentRequest updateCommentRequest,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @PathVariable Long commentId) {
        return commentService.updateComment(updateCommentRequest, customUserDetails.getId(), commentId);
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @PathVariable Long commentId) {
        commentService.deleteComment(customUserDetails.getId(), commentId);
    }

    @GetMapping
    public List<CommentDto> getCommentByOwner(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getCommentsByOwner(customUserDetails.getId(), from, size);
    }
}
