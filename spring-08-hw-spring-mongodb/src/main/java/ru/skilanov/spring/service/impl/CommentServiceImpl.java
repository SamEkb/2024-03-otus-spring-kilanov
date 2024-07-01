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
    public Optional<CommentDto> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return repository.findAllByBookId(bookId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto create(String description, String bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(EntityNotFoundException::new);
        var comment = Comment.builder()
                .description(description)
                .book(book)
                .build();

        var savedComment = repository.save(comment);
        return mapper.toDto(savedComment);
    }

    @Transactional
    @Override
    public CommentDto update(String id, String description) {
        var comment = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        comment.setDescription(description);

        var savedComment = repository.save(comment);
        return mapper.toDto(savedComment);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllByBookId(String id) {
        repository.deleteAllByBookId(id);
    }
}
