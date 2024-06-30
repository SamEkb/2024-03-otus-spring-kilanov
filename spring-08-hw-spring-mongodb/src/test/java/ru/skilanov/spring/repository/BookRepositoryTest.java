package ru.skilanov.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.listener.BookCascadeDeleteListener;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;
import ru.skilanov.spring.repositories.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Books repository test")
@DataMongoTest
@Testcontainers
@Import(BookCascadeDeleteListener.class)
class BookRepositoryTest {

    private static final String DEFAULT_ID_ONE = "1";

    private static final String EXPECTED_ID = "3";

    private static final String BOOK_TITLE_KARENINA = "Anna Karenina";

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @DisplayName("Add book to db")
    @Test
    void shouldInsertBook() {
        var expectedBook = Book.builder().title(BOOK_TITLE_KARENINA)
                .author(authorRepository.findById(DEFAULT_ID_ONE).orElse(null))
                        .genre(genreRepository.findById(DEFAULT_ID_ONE).orElse(null))
                                .build();
        bookRepository.save(expectedBook);
        Optional<Book> actualBook = bookRepository.findById(EXPECTED_ID);
        actualBook.ifPresent(it -> assertThat(it.getTitle()).isEqualTo(expectedBook.getTitle()));
    }

    @DisplayName("Update book to db")
    @Test
    void shouldUpdateBook() {
        var updatedBook = new Book(DEFAULT_ID_ONE, BOOK_TITLE_KARENINA,
                authorRepository.findById(DEFAULT_ID_ONE).orElse(null),
                genreRepository.findById(DEFAULT_ID_ONE).orElse(null));
        bookRepository.save(updatedBook);

        Book foundBook = bookRepository.findById(DEFAULT_ID_ONE)
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundBook.getTitle()).isEqualTo(BOOK_TITLE_KARENINA);
    }

    @DisplayName("Returns expected book")
    @Test
    void shouldReturnExpectedBookById() {
        Book expectedBook = new Book(DEFAULT_ID_ONE, BOOK_TITLE_KARENINA,
                authorRepository.findById(DEFAULT_ID_ONE).orElse(null),
                genreRepository.findById(DEFAULT_ID_ONE).orElse(null));

        Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());
        actualBook.ifPresent(it -> assertThat(it.getTitle()).isEqualTo(expectedBook.getTitle()));
    }

    @DisplayName("Deletes expected book by id")
    @Test
    void shouldCorrectDeleteBookById() {
        Book bookForDeleting = bookRepository.findAll().get(0);

        var commentsBeforeDeleting = commentRepository.findAllByBookId(bookForDeleting.getId());

        assertThat(commentsBeforeDeleting).isNotEmpty();

        bookRepository.delete(bookForDeleting);

        var commentsAfterDeleting = commentRepository.findAllByBookId(bookForDeleting.getId());

        assertThat(commentsAfterDeleting).isEmpty();

        Book deletedBook = bookRepository.findById(bookForDeleting.getId()).orElse(null);

        assertThat(deletedBook).isNull();
    }

    @DisplayName("Returns expected books list")
    @Test
    void shouldReturnExpectedBooksList() {
        int actualPersonListSize = bookRepository.findAll().size();
        assertThat(actualPersonListSize)
                .isEqualTo(bookRepository.count());
    }
}