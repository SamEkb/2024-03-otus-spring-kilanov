package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.request.BookCreateDto;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.request.BookUpdateDto;
import ru.skilanov.spring.exception.NotFoundException;
import ru.skilanov.spring.mapper.BookMapper;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.GenreRepository;
import ru.skilanov.spring.service.api.BookService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto dto) {
        var genre = genreRepository.findById(dto.getGenreId()).orElseThrow(NotFoundException::new);
        var author = authorRepository.findById(dto.getAuthorId()).orElseThrow(NotFoundException::new);
        var book = Book.builder()
                .title(dto.getTitle())
                .genre(genre)
                .author(author)
                .build();

        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional
    @Override
    public BookDto update(BookUpdateDto dto) {
        var oldBook = bookRepository.findById(dto.getId()).orElseThrow(NotFoundException::new);
        var genre = genreRepository.findById(dto.getGenreId()).orElseThrow(NotFoundException::new);
        var author = authorRepository.findById(dto.getAuthorId()).orElseThrow(NotFoundException::new);
        var book = Book.builder()
                .id(oldBook.getId())
                .title(dto.getTitle())
                .genre(genre)
                .author(author)
                .build();

        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
