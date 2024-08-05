package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.CommentDto;
import ru.skilanov.spring.exception.NotFoundException;
import ru.skilanov.spring.mapper.CommentMapper;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;
import ru.skilanov.spring.service.api.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final BookRepository bookRepository;

    private final CommentMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NotFoundException::new);
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
        var book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        var comment = Comment.builder()
                .description(description)
                .book(book)
                .build();

        var savedComment = repository.save(comment);
        return mapper.toDto(savedComment);
    }

    @Transactional
    @Override
    public CommentDto update(long id, String description) {
        var oldComment = repository.findById(id).orElseThrow(NotFoundException::new);
        oldComment.setDescription(description);

        var savedComment = repository.save(oldComment);
        return mapper.toDto(savedComment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
