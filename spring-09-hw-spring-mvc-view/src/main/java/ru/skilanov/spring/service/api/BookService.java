package ru.skilanov.spring.service.api;


import ru.skilanov.spring.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookDto dto);

    BookDto update(BookDto dto);

    void deleteById(long id);
}
