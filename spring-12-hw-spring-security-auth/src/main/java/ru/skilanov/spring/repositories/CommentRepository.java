package ru.skilanov.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.skilanov.spring.models.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByBookId(Long id);
}
