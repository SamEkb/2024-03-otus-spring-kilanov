package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GenreServiceImpl.class})
public class GenreServiceImplTest {

    public static final long ID = 1L;

    @Autowired
    private GenreServiceImpl genreService;

    @MockBean
    private GenreRepository genreRepository;

    private Genre expectedGenre;

    @BeforeEach
    public void setUp() {
        expectedGenre = new Genre(ID, "Genre_" + ID);
    }

    @Test
    public void whenFindGenreByIdThenItReturns() {
        when(genreRepository.findById(ID)).thenReturn(Optional.of(expectedGenre));

        var genre = genreService.findById(ID);

        verify(genreRepository, times(1)).findById(ID);

        assertThat(genre).isEqualTo(Optional.of(expectedGenre));
    }

    @Test
    public void whenFindAllGenresThenAllFound() {
        var expectedGenres = getDbGenres();
        when(genreRepository.findAll()).thenReturn(expectedGenres);

        var genres = genreService.findAll();

        verify(genreRepository, times(1)).findAll();

        Assertions.assertThat(genres)
                .containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    @Test
    public void whenInsertGenreThenItInserted() {
        var newGenre = new Genre(0, "Genre_" + ID);
        when(genreRepository.save(newGenre)).thenReturn(expectedGenre);

        var resultGenre = genreService.insert("Genre_" + ID);

        verify(genreRepository, times(1)).save(newGenre);

        assertThat(resultGenre).isEqualTo(expectedGenre);
    }

    @Test
    public void whenUpdateGenreThenItUpdated() {
        var updatedGenre = new Genre(ID, "Genre_" + 2L);
        when(genreRepository.save(updatedGenre)).thenReturn(updatedGenre);

        var resultGenre = genreService.update(ID, "Genre_" + 2L);

        verify(genreRepository, times(1)).save(updatedGenre);

        assertThat(resultGenre).isEqualTo(updatedGenre);
    }

    @Test
    public void whenDeleteGenreThenItDeleted() {
        doNothing().when(genreRepository).deleteById(ID);
        genreService.deleteById(ID);
        verify(genreRepository, times(1)).deleteById(ID);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
