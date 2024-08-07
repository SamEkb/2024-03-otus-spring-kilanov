package ru.skilanov.spring.mapper;

import org.mapstruct.Mapper;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.models.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto dto);
}
