package ru.skilanov.spring.mapper;

import org.mapstruct.Mapper;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.models.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toDto(Genre genre);

    Genre toEntity(GenreDto dto);
}
