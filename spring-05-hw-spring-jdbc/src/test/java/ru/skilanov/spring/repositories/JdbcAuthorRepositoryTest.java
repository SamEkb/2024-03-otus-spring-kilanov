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
import ru.skilanov.spring.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами ")
@JdbcTest
@Import({JdbcAuthorRepository.class, JdbcBookRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class JdbcAuthorRepositoryTest {

    public static final long ID = 1L;
    @Autowired
    private JdbcAuthorRepository authorRepository;

    @Autowired
    private JdbcBookRepository bookRepository;

    private List<Author> dbAuthors;

    @BeforeEach
    public void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void whenFindAuthorByIdThenItReturns(Author expectedAuthor) {
        var actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void whenFindAllAuthorsThenTheyReturn() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName("должен сохранять нового автора")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    void whenSaveAuthorThenItSaved() {
        var expectedAuthor = new Author(0, "Pushkin");
        var returnedAuthor = authorRepository.save(expectedAuthor);
        assertThat(returnedAuthor).isNotNull()
                .matches(author -> author.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedAuthor);

        assertThat(authorRepository.findById(returnedAuthor.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedAuthor);
    }

    @DisplayName("должен сохранять измененного автора")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    void whenSaveUpdatedAuthorThenItReturnsUpdated() {
        var expectedAuthor = new Author(ID, "Gogol");

        assertThat(authorRepository.findById(expectedAuthor.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedAuthor);

        var returnedAuthor = authorRepository.save(expectedAuthor);
        assertThat(returnedAuthor).isNotNull()
                .matches(author -> author.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedAuthor);

        assertThat(authorRepository.findById(returnedAuthor.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedAuthor);
    }

    @DisplayName("должен удалять автора по id ")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Test
    void whenDeleteAuthorThenItDeleted() {
        assertThat(authorRepository.findById(ID)).isPresent();
        bookRepository.deleteById(ID);
        authorRepository.deleteById(ID);
        assertThat(authorRepository.findById(ID)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
