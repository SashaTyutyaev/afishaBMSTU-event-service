package ru.afishaBMSTU.users.comments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.afishaBMSTU.users.comments.model.CommentDto;
import ru.afishaBMSTU.users.comments.model.NewCommentDto;
import ru.afishaBMSTU.users.comments.model.UpdateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid NewCommentDto newCommentDto, @PathVariable Long userId,
                                    @RequestParam Long eventId) {
        return commentService.createComment(newCommentDto, userId, eventId);
    }

    @PatchMapping("{commentId}")
    public CommentDto updateComment(@RequestBody UpdateCommentRequest updateCommentRequest, @PathVariable Long userId,
                                    @PathVariable Long commentId) {
        return commentService.updateComment(updateCommentRequest, userId, commentId);
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getCommentByOwner(@PathVariable Long userId, @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getCommentByOwner(userId, from, size);
    }
}
