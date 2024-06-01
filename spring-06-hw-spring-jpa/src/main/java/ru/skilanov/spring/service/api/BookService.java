package ru.skilanov.spring.service.api;


import ru.skilanov.spring.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Book insert(String title, long authorId, long genreId);

    Book update(Book book);

    void deleteById(long id);
}
