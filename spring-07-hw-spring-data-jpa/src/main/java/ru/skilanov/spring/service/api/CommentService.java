package ru.skilanov.spring.service.api;

import ru.skilanov.spring.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto create(String description, long bookId);

    CommentDto update(long id, String description, long bookId);

    void deleteById(long id);
}
