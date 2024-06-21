package ru.skilanov.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.skilanov.spring.models.Book;

public interface BookRepository extends MongoRepository<Book, String> {

}
