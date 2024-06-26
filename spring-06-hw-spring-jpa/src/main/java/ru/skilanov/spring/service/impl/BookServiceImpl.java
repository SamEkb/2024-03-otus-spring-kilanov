package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.mapper.BookMapper;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.api.AuthorRepository;
import ru.skilanov.spring.repositories.api.BookRepository;
import ru.skilanov.spring.repositories.api.GenreRepository;
import ru.skilanov.spring.service.api.BookService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
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
    public BookDto create(String title, long authorId, long genreId) {
        return save(0, title, authorId, genreId);
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.delete(id);
    }

    private BookDto save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId).orElseThrow(EntityNotFoundException::new);
        var genre = genreRepository.findById(genreId).orElseThrow(EntityNotFoundException::new);
        var book = Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .genre(genre)
                .build();
        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }
}
