package ru.skilanov.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.repositories.AuthorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Authors repository test")
@DataMongoTest
@Testcontainers
public class AuthorRepositoryTest {

    private static final String DEFAULT_ID_ONE = "1";

    private static final String EXPECTED_AUTHOR_DOSTOEVSKY = "Dostoevsky";

    private static final String DEFAULT_AUTHOR_TOLSTOY = "Tolstoy";

    private static final String EXPECTED_ID = "3";

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private AuthorRepository authorRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @DisplayName("Add author to db")
    @Test
    void shouldInsertAuthor() {
        var expectedAuthor = Author.builder()
                .fullName(EXPECTED_AUTHOR_DOSTOEVSKY)
                .build();
        authorRepository.save(expectedAuthor);
        Optional<Author> actualAuthor = authorRepository.findById(EXPECTED_ID);
        actualAuthor.ifPresent(it -> assertThat(it.getFullName()).isEqualTo(expectedAuthor.getFullName()));
    }

    @DisplayName("Returns expected author")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(DEFAULT_ID_ONE, DEFAULT_AUTHOR_TOLSTOY);
        Optional<Author> actualAuthor = authorRepository.findById(expectedAuthor.getId());
        actualAuthor.ifPresent(it -> assertThat(it).usingRecursiveComparison().isEqualTo(expectedAuthor));
    }

    @DisplayName("Deletes expected author by id")
    @Test
    void shouldCorrectDeleteAuthorById() {
        Author actualAuthorList = authorRepository.findAll().get(0);

        authorRepository.delete(actualAuthorList);

        Author deletedAuthor = authorRepository.findById(actualAuthorList.getId())
                .orElse(null);

        assertThat(deletedAuthor).isNull();
    }

    @DisplayName("Returns expected authors list")
    @Test
    void shouldReturnExpectedAuthorsList() {
        int actualPersonListSize = authorRepository.findAll().size();
        assertThat(actualPersonListSize)
                .isEqualTo(authorRepository.count());
    }
}
