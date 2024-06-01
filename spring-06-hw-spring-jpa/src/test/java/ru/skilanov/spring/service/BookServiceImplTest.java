package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.api.AuthorRepository;
import ru.skilanov.spring.repositories.api.BookRepository;
import ru.skilanov.spring.repositories.api.GenreRepository;
import ru.skilanov.spring.service.api.AuthorService;
import ru.skilanov.spring.service.api.GenreService;
import ru.skilanov.spring.service.impl.AuthorServiceImpl;
import ru.skilanov.spring.service.impl.BookServiceImpl;
import ru.skilanov.spring.service.impl.GenreServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {BookServiceImpl.class})
public class BookServiceImplTest {

    public static final long ID = 1L;

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    @MockBean
    private BookRepository bookRepository;

    private Genre genre;

    private Author author;

    private Book expectedBook;

    @BeforeEach
    public void setUp() {
        genre = new Genre(ID, "Genre_" + ID);
        author = new Author(ID, "Author_" + ID);
        expectedBook = Book.builder()
                .id(ID)
                .title("Book_" + ID)
                .author(author)
                .genre(genre)
                .build();
    }

    @Test
    public void whenFindBookByIdThenItReturns() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(expectedBook));

        var book = bookService.findById(ID);

        verify(bookRepository, times(1)).findById(ID);

        assertThat(book).isEqualTo(Optional.of(expectedBook));
    }

    @Test
    public void whenFindAllBooksThenAllFound() {
        var expectedBooks = getDbBooks();
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        var books = bookService.findAll();

        verify(bookRepository, times(1)).findAll();

        Assertions.assertThat(books)
                .containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @Test
    public void whenInsertBookThenItInserted() {
        var newBook = Book.builder()
                .id(0)
                .title("Book_" + ID)
                .author(author)
                .genre(genre)
                .build();

        when(bookRepository.save(newBook)).thenReturn(expectedBook);
        when(authorService.findById(ID)).thenReturn(Optional.of(author));
        when(genreService.findById(ID)).thenReturn(Optional.of(genre));

        var resultBook = bookService.insert("Book_" + ID, ID, ID);

        verify(bookRepository, times(1)).save(newBook);

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void whenUpdateBookThenItUpdated() {
        var updatedBook = Book.builder()
                .id(0)
                .title("Book_" + 2L)
                .author(author)
                .genre(genre)
                .build();
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(authorService.findById(ID)).thenReturn(Optional.of(author));
        when(genreService.findById(ID)).thenReturn(Optional.of(genre));

        var resultBook = bookService.update(updatedBook);

        verify(bookRepository, times(1)).save(updatedBook);

        assertThat(resultBook).isEqualTo(updatedBook);
    }

    @Test
    public void whenDeleteBookThenItDeleted() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(expectedBook));
        doNothing().when(bookRepository).delete(expectedBook);
        bookService.deleteById(ID);
        verify(bookRepository, times(1)).delete(expectedBook);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> Book.builder()
                        .id(id)
                        .title("BookTitle_" + id)
                        .author(dbAuthors.get(id - 1))
                        .genre(dbGenres.get(id - 1))
                        .build()
                )
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
