package ru.skilanov.spring.service.api;

import ru.skilanov.spring.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto create(String description, String bookId);

    CommentDto update(String id, String description, String bookId);

    void deleteById(String id);
}
