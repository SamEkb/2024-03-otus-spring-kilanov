package ru.skilanov.spring.mapper;

import org.mapstruct.Mapper;
import ru.skilanov.spring.dto.CommentDto;
import ru.skilanov.spring.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto dto);
}
