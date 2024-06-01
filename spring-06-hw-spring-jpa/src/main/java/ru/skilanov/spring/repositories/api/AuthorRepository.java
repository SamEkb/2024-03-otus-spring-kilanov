package ru.skilanov.spring.repositories.api;


import ru.skilanov.spring.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();

    Optional<Author> findById(long id);

    Author save(Author author);

    void delete(Author author);
}
