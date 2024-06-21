package ru.skilanov.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.skilanov.spring.models.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {

}
