package ru.afishaBMSTU.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.comment.NewCommentDto;
import ru.afishaBMSTU.model.comment.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {DateTimeFormatter.class, LocalDateTime.class})
public interface CommentMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Comment toComment(NewCommentDto newCommentDto);

    @Mapping(target = "createdAt", expression = "java(FORMATTER.format(comment.getCreatedAt()))")
    CommentDto toCommentDto(Comment comment);

}
