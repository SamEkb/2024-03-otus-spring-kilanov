package ru.skilanov.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.request.BookCreateDto;
import ru.skilanov.spring.dto.request.BookUpdateDto;
import ru.skilanov.spring.service.api.BookService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/api/books")
    public BookDto createBook(@RequestBody BookCreateDto dto) {
        return bookService.create(dto);
    }

    @PatchMapping(value = "/api/books/{id}")
    public BookDto updateBook(@RequestBody BookUpdateDto dto) {
        return bookService.update(dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/books/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }
}
