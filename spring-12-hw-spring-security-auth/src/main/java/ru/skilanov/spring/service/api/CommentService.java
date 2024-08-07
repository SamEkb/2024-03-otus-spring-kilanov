package ru.skilanov.spring.service.api;

import ru.skilanov.spring.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto create(String description, long bookId);

    CommentDto update(long id, String description);

    void deleteById(long id);
}
