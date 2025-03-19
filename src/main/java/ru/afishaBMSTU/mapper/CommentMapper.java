package ru.afishaBMSTU.mapper;

import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.comment.NewCommentDto;
import ru.afishaBMSTU.model.comment.Comment;

import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getUser().getId())
                .createdAt(FORMATTER.format(comment.getCreatedAt()))
                .build();
    }
}
