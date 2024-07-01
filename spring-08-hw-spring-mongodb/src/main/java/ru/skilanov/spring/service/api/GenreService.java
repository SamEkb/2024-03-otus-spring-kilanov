package ru.skilanov.spring.service.api;

import ru.skilanov.spring.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Optional<GenreDto> findById(String id);

    List<GenreDto> findAll();

    GenreDto create(String name);

    GenreDto update(String id, String name);

    void deleteById(String id);
}
