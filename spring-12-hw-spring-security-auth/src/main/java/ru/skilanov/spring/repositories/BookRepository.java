package ru.skilanov.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skilanov.spring.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "bookWithAuthorAndGenreGraph")
    @Override
    Optional<Book> findById(Long id);

    @EntityGraph(value = "bookWithAuthorAndGenreGraph")
    @Override
    List<Book> findAll();
}
