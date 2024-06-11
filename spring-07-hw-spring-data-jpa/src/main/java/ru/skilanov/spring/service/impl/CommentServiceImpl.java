package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.CommentDto;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.mapper.CommentMapper;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;
import ru.skilanov.spring.service.api.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final BookRepository bookRepository;

    private final CommentMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        return repository.findAllByBookId(bookId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto create(String description, long bookId) {
        return save(0, description, bookId);
    }

    @Transactional
    @Override
    public CommentDto update(long id, String description, long bookId) {
        return save(id, description, bookId);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        var comment = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.delete(comment);
    }

    private CommentDto save(long id, String description, long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(EntityNotFoundException::new);
        var comment = Comment.builder()
                .id(id)
                .description(description)
                .book(book)
                .build();

        var savedComment = repository.save(comment);
        return mapper.toDto(savedComment);
    }
}
