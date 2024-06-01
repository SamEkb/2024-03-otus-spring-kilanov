package ru.skilanov.spring.service.api;

import ru.skilanov.spring.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> findById(long id);

    List<Genre> findAll();

    Genre insert(String name);

    Genre update(long id, String name);

    void deleteById(long id);
}
