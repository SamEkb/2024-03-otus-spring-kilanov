package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.repositories.api.CommentRepository;
import ru.skilanov.spring.service.api.BookService;
import ru.skilanov.spring.service.api.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final BookService bookService;

    @Override
    public Optional<Comment> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        return repository.findAllByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(String description, long bookId) {
        var book = bookService.findById(bookId).orElseThrow(EntityNotFoundException::new);
        var comment = Comment.builder()
                .description(description)
                .book(book)
                .build();
        return repository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(long id, String description) {
        var comment = Comment.builder()
                .id(id)
                .description(description)
                .build();
        return repository.save(comment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var comment = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.delete(comment);
    }
}
