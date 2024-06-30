package ru.skilanov.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.skilanov.spring.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByBookId(String id);

    void deleteAllByBookId(String bookId);
}
