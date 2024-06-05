package ru.skilanov.spring.converters;

import org.springframework.stereotype.Component;
import ru.skilanov.spring.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
