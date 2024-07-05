package ru.skilanov.spring.service.api;

import ru.skilanov.spring.dto.GenreDto;

import java.util.List;

public interface GenreService {
    GenreDto findById(long id);

    List<GenreDto> findAll();

    GenreDto create(String name);

    GenreDto update(long id, String name);

    void deleteById(long id);
}
