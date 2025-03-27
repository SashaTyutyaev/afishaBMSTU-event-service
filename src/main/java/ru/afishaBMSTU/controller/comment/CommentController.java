package ru.afishaBMSTU.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.afishaBMSTU.dto.jwt.JwtTokenDataDto;
import ru.afishaBMSTU.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "API для работы с комментариями")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Создать комментарий", description = "Добавление нового комментария к мероприятию")
    @ApiResponse(responseCode = "201", description = "Комментарий создан",
            content = @Content(schema = @Schema(implementation = CommentDto.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @RequestBody @Valid NewCommentDto newCommentDto,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID мероприятия", required = true, example = "1")
            @RequestParam Long eventId) {
        return commentService.createComment(newCommentDto, jwtTokenDataDto.getExternalId(), eventId);
    }

    @Operation(summary = "Обновить комментарий", description = "Редактирование существующего комментария")
    @ApiResponse(responseCode = "200", description = "Комментарий обновлен",
            content = @Content(schema = @Schema(implementation = CommentDto.class)))
    @PatchMapping("{commentId}")
    public CommentDto updateComment(
            @RequestBody @Valid UpdateCommentRequest updateCommentRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID комментария", required = true, example = "1")
            @PathVariable Long commentId) {
        return commentService.updateComment(updateCommentRequest, jwtTokenDataDto.getExternalId(), commentId);
    }

    @Operation(summary = "Удалить комментарий", description = "Удаление комментария по ID")
    @ApiResponse(responseCode = "204", description = "Комментарий удален")
    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID комментария", required = true, example = "1")
            @PathVariable Long commentId) {
        commentService.deleteComment(jwtTokenDataDto.getExternalId(), commentId);
    }

    @Operation(summary = "Получить комментарии пользователя",
            description = "Получение списка комментариев текущего пользователя")
    @GetMapping
    public List<CommentDto> getCommentByOwner(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "Начальная позиция", example = "0") @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов", example = "10") @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getCommentsByOwner(jwtTokenDataDto.getExternalId(), from, size);
    }
}