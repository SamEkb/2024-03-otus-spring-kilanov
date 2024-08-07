package ru.skilanov.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skilanov.spring.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
