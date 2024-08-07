package ru.skilanov.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skilanov.spring.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
