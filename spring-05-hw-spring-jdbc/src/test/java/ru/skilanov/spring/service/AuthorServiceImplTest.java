package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AuthorServiceImpl.class})
public class AuthorServiceImplTest {

    public static final long ID = 1L;

    @Autowired
    private AuthorServiceImpl authorService;

    @MockBean
    private AuthorRepository authorRepository;

    private Author expectedAuthor;

    @BeforeEach
    public void setUp() {
        expectedAuthor = new Author(ID, "Author_" + ID);
    }

    @Test
    public void whenFindAuthorByIdThenItReturns() {
        when(authorRepository.findById(ID)).thenReturn(Optional.of(expectedAuthor));

        var author = authorService.findById(ID);

        verify(authorRepository, times(1)).findById(ID);

        assertThat(author).isEqualTo(Optional.of(expectedAuthor));
    }

    @Test
    public void whenFindAllAuthorsThenAllFound() {
        var expectedAuthors = getDbAuthors();
        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        var authors = authorService.findAll();

        verify(authorRepository, times(1)).findAll();

        Assertions.assertThat(authors)
                .containsExactlyInAnyOrderElementsOf(expectedAuthors);
    }

    @Test
    public void whenInsertAuthorThenItInserted() {
        var newAuthor = new Author(0, "Author_" + ID);
        when(authorRepository.save(newAuthor)).thenReturn(expectedAuthor);

        var resultAuthor = authorService.insert("Author_" + ID);

        verify(authorRepository, times(1)).save(newAuthor);

        assertThat(resultAuthor).isEqualTo(expectedAuthor);
    }

    @Test
    public void whenUpdateAuthorThenItUpdated() {
        var updatedAuthor = new Author(ID, "Author_" + 2L);
        when(authorRepository.save(updatedAuthor)).thenReturn(updatedAuthor);

        var resultAuthor = authorService.update(ID, "Author_" + 2L);

        verify(authorRepository, times(1)).save(updatedAuthor);

        assertThat(resultAuthor).isEqualTo(updatedAuthor);
    }

    @Test
    public void whenDeleteAuthorThenItDeleted() {
        doNothing().when(authorRepository).deleteById(ID);
        authorService.deleteById(ID);
        verify(authorRepository, times(1)).deleteById(ID);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                .mapToObj(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
