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
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Genres repository test")
@DataMongoTest
@Testcontainers
public class GenreRepositoryTest {
    private static final String DEFAULT_ID_ONE = "1";

    private static final String DEFAULT_GENRE_DRAMA = "Drama";

    private static final String EXPECTED_GENRE = "Thriller";

    private static final String EXPECTED_ID = "3";

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private GenreRepository genreRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @DisplayName("Add genre to db")
    @Test
    void shouldInsertGenre() {
        var expectedGenre = Genre.builder()
                .name(EXPECTED_GENRE)
                .build();
        genreRepository.save(expectedGenre);
        Optional<Genre> actualGenre = genreRepository.findById(EXPECTED_ID);
        actualGenre.ifPresent(it -> assertThat(it.getName()).isEqualTo(expectedGenre.getName()));
    }

    @DisplayName("Returns expected genre")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(DEFAULT_ID_ONE, DEFAULT_GENRE_DRAMA);
        Optional<Genre> actualGenre = genreRepository.findById(expectedGenre.getId());
        actualGenre.ifPresent(it -> assertThat(it).usingRecursiveComparison().isEqualTo(expectedGenre));
    }

    @DisplayName("Deletes expected genre by id")
    @Test
    void shouldCorrectDeleteGenreById() {
        Genre genreForDeleting = genreRepository.findAll().get(0);
        genreRepository.delete(genreForDeleting);

        Genre deletedGenre = genreRepository.findById(genreForDeleting.getId())
                .orElse(null);

        assertThat(deletedGenre).isNull();
    }

    @DisplayName("Returns expected genre list")
    @Test
    void shouldReturnExpectedGenreList() {
        int actualPersonListSize = genreRepository.findAll().size();
        assertThat(actualPersonListSize)
                .isEqualTo(genreRepository.count());
    }
}
