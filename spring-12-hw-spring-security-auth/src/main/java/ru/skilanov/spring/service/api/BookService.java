package ru.skilanov.spring.service.api;


import ru.skilanov.spring.dto.request.BookCreateDto;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.request.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookCreateDto dto);

    BookDto update(BookUpdateDto dto);

    void deleteById(long id);
}
