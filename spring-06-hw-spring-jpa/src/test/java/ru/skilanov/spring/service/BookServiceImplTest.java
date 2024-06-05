package ru.skilanov.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.mapper.AuthorMapperImpl;
import ru.skilanov.spring.mapper.BookMapperImpl;
import ru.skilanov.spring.mapper.CommentMapperImpl;
import ru.skilanov.spring.mapper.GenreMapperImpl;
import ru.skilanov.spring.repositories.api.BookRepository;
import ru.skilanov.spring.repositories.impl.AuthorRepositoryJpa;
import ru.skilanov.spring.repositories.impl.BookRepositoryJpa;
import ru.skilanov.spring.repositories.impl.GenreRepositoryJpa;
import ru.skilanov.spring.service.api.BookService;
import ru.skilanov.spring.service.impl.AuthorServiceImpl;
import ru.skilanov.spring.service.impl.BookServiceImpl;
import ru.skilanov.spring.service.impl.GenreServiceImpl;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookServiceImpl.class, BookRepositoryJpa.class,
        AuthorServiceImpl.class, GenreServiceImpl.class,
        AuthorRepositoryJpa.class, GenreRepositoryJpa.class,
        AuthorMapperImpl.class, GenreMapperImpl.class,
        BookMapperImpl.class, CommentMapperImpl.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceImplTest {

    public static final long ID = 1L;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private GenreDto genre;

    private AuthorDto author;

    private BookDto savedBook;

    private BookDto expectedBook;

    @BeforeEach
    public void setUp() {
        genre = new GenreDto(ID, "Genre_" + ID);
        author = new AuthorDto(ID, "Author_" + ID);
        savedBook = new BookDto(4L, "Book_" + ID, author, genre);
        expectedBook = new BookDto(ID, "Book_" + ID, author, genre);
    }

    @Test
    public void whenFindBookByIdThenItReturns() {
        var book = bookService.findById(ID);

        assertThat(book).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    public void whenFindAllBooksThenAllFound() {
        var expectedBooks = getDbBooks();

        var books = bookService.findAll();

        assertThat(books).hasSize(expectedBooks.size());
    }

    @Test
    public void whenInsertBookThenItInserted() {
        var resultBook = bookService.create("Book_" + ID, ID, ID);

        assertThat(resultBook).isEqualTo(savedBook);
    }

    @Test
    public void whenUpdateBookThenItUpdated() {
        var updatedBook = new BookDto(ID, "Book_" + 2L, author, genre);

        var resultBook = bookService.update(ID, "Book_" + 2L, ID, ID);

        assertThat(resultBook).isEqualTo(updatedBook);
    }

    @Test
    public void whenDeleteBookThenItDeleted() {
        assertThat(bookService.findById(ID)).isPresent();
        bookService.deleteById(ID);
        assertThat(bookService.findById(ID)).isNotPresent();
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> BookDto.builder()
                        .id(id)
                        .title("BookTitle_" + id)
                        .author(dbAuthors.get(id - 1))
                        .genre(dbGenres.get(id - 1))
                        .build()
                )
                .toList();
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
