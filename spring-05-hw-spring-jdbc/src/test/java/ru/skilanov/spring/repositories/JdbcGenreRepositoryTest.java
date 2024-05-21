package ru.skilanov.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import({JdbcGenreRepository.class, JdbcBookRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class JdbcGenreRepositoryTest {

    public static final long ID = 1L;
    @Autowired
    private JdbcGenreRepository genreRepository;

    @Autowired
    private JdbcBookRepository bookRepository;

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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    @DisplayName("должен удалять жанр по id ")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    void whenDeleteGenreThenItDeleted() {
        assertThat(genreRepository.findById(ID)).isPresent();
        bookRepository.deleteById(ID);
        genreRepository.deleteById(ID);
        assertThat(genreRepository.findById(ID)).isEmpty();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

}
