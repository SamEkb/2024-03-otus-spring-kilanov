package ru.skilanov.spring.repositories;


import ru.skilanov.spring.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> findAll();

    Optional<Genre> findById(long id);

    Genre save(Genre genre);

    void deleteById(long id);
}
