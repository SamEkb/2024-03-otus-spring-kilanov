package ru.skilanov.spring.service.api;

import ru.skilanov.spring.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long bookId);

    Comment insert(String description, long bookId);

    Comment update(long id, String description);

    void deleteById(long id);
}
