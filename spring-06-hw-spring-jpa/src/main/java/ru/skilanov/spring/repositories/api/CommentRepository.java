package ru.skilanov.spring.repositories.api;

import ru.skilanov.spring.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long bookId);

    Comment save(Comment comment);

    void delete(long id);
}
