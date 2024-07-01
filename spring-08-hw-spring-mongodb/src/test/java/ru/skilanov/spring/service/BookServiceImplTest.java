package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.dto.AuthorDto;
import ru.skilanov.spring.dto.BookDto;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.mapper.BookMapper;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.models.Genre;
import ru.skilanov.spring.repositories.AuthorRepository;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;
import ru.skilanov.spring.repositories.GenreRepository;
import ru.skilanov.spring.service.impl.BookServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@DisplayName("Books service test")
@SpringBootTest(classes = {BookServiceImpl.class})
public class BookServiceImplTest {

    public static final String BOOK_TITLE_KARENINA = "Anna Karenina";

    public static final String BOOK_TITLE_WAR_AND_PEACE = "Voina I Mir";

    public static final String ONE_ID = "1";

    public static final int ONE = 1;

    public static final String TWO_ID = "2";


    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Creat book")
    @Test
    public void whenCreateBookThenItCreated() {
        var author = new Author();
        var genre = new Genre();
        Book book1 = new Book(null, BOOK_TITLE_KARENINA, author, genre);
        Book savedBook = new Book(ONE_ID, BOOK_TITLE_KARENINA, author, genre);
        when(bookRepository.save(book1)).thenReturn(savedBook);
        when(authorRepository.findById(ONE_ID)).thenReturn(Optional.of(author));
        when(genreRepository.findById(ONE_ID)).thenReturn(Optional.of(genre));
        bookService.create(BOOK_TITLE_KARENINA, ONE_ID, ONE_ID);
        verify(bookRepository, times(ONE)).save(any());
    }

    @DisplayName("Update book")
    @Test
    public void whenUpdateBookThenItUpdated() {
        var author = new Author();
        var genre = new Genre();
        Book book1 = new Book(ONE_ID, BOOK_TITLE_KARENINA, new Author(), new Genre());
        when(bookRepository.save(book1)).thenReturn(book1);
        when(bookRepository.findById(ONE_ID)).thenReturn(Optional.of(book1));
        when(authorRepository.findById(ONE_ID)).thenReturn(Optional.of(author));
        when(genreRepository.findById(ONE_ID)).thenReturn(Optional.of(genre));
        bookService.update(ONE_ID, BOOK_TITLE_KARENINA, ONE_ID, ONE_ID);
        verify(bookRepository, times(ONE)).save(any());
    }

    @DisplayName("Get book")
    @Test
    public void whenGetBookThenItReturns() {
        Book book1 = new Book(ONE_ID, BOOK_TITLE_KARENINA, new Author(), new Genre());
        BookDto dto = new BookDto(ONE_ID, BOOK_TITLE_KARENINA, new AuthorDto(), new GenreDto());
        when(bookRepository.findById(ONE_ID)).thenReturn(Optional.of(book1));
        when(bookMapper.toDto(book1)).thenReturn(dto);
        BookDto book = bookService.findById(ONE_ID).orElseThrow(EntityNotFoundException::new);
        AssertionsForClassTypes.assertThat(book.getTitle()).isEqualTo(BOOK_TITLE_KARENINA);
    }

    @DisplayName("Get books")
    @Test
    public void whenGetAllBooksThenAllReturns() {
        Book book1 = new Book(ONE_ID, BOOK_TITLE_KARENINA, new Author(), new Genre());
        Book book2 = new Book(TWO_ID, BOOK_TITLE_WAR_AND_PEACE, new Author(), new Genre());
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> books = bookRepository.findAll();

        Assertions.assertThat(books)
                .containsExactlyInAnyOrder(book1, book2);
    }

    @DisplayName("Delete book")
    @Test
    public void whenDeleteBookThenItDeleted() {
        bookService.deleteById(ONE_ID);
        verify(bookRepository, times(ONE)).deleteById(ONE_ID);
        verify(commentRepository, times(ONE)).deleteAllByBookId(ONE_ID);
    }
}

