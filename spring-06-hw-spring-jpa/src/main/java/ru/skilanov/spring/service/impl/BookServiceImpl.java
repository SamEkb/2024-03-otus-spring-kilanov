package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.api.BookRepository;
import ru.skilanov.spring.service.api.AuthorService;
import ru.skilanov.spring.service.api.BookService;
import ru.skilanov.spring.service.api.GenreService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, long authorId, long genreId) {
        var author = authorService.findById(authorId).orElseThrow(EntityNotFoundException::new);
        var genre = genreService.findById(genreId).orElseThrow(EntityNotFoundException::new);
        var book = Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .build();
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        bookRepository.delete(book);
    }
}
