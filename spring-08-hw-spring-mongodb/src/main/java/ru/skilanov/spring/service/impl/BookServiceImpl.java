package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.mapper.BookMapper;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;
import ru.skilanov.spring.repositories.GenreRepository;
import ru.skilanov.spring.service.api.BookService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(String id) {
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
    public BookDto create(String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId).orElseThrow(EntityNotFoundException::new);
        var genre = genreRepository.findById(genreId).orElseThrow(EntityNotFoundException::new);
        var book = Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .build();

        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional
    @Override
    public BookDto update(String id, String title, String authorId, String genreId) {
        var book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        var author = authorRepository.findById(authorId).orElseThrow(EntityNotFoundException::new);
        var genre = genreRepository.findById(genreId).orElseThrow(EntityNotFoundException::new);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);

        var savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
        commentRepository.deleteAllByBookId(id);
    }

}
