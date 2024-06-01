package ru.skilanov.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.impl.GenreRepositoryJpa;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import({GenreRepositoryJpa.class})
public class GenreRepositoryJpaTest {

    public static final long ID = 1L;
    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<Genre> dbGenres;

    @BeforeEach
    public void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void whenFindGenreByIdThenItReturns(Genre expectedGenre) {
        var actualGenre = genreRepository.findById(expectedGenre.getId());
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void whenFindAllGenresThenTheyReturn() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен сохранять новый жанр")
    @Test
    void whenSaveGenreThenItSaved() {
        var expectedGenre = new Genre(0, "detective");
        var returnedGenre = genreRepository.save(expectedGenre);
        assertThat(returnedGenre).isNotNull()
                .matches(genre -> genre.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedGenre);

        assertThat(genreRepository.findById(returnedGenre.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedGenre);
    }

    @DisplayName("должен сохранять измененный жанр")
    @Test
    void whenSaveUpdatedGenreThenItReturnsUpdated() {
        var expectedGenre = new Genre(ID, "sci-fy");

        assertThat(genreRepository.findById(expectedGenre.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedGenre);

        var returnedGenre = genreRepository.save(expectedGenre);
        assertThat(returnedGenre).isNotNull()
                .matches(genre -> genre.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedGenre);

        assertThat(genreRepository.findById(returnedGenre.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedGenre);
    }

    @DisplayName("должен удалять жанр")
    @Test
    void whenDeleteGenreThenItDeleted() {
        var genre = genreRepository.findById(ID).orElse(null);
        genreRepository.delete(genre);
        assertThat(genreRepository.findById(ID)).isEmpty();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

}
