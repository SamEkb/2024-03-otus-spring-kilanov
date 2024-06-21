package ru.skilanov.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.skilanov.spring.models.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
