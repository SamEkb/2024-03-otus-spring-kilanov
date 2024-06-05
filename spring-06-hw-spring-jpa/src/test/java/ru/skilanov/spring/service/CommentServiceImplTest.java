package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
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
import ru.skilanov.spring.dto.CommentDto;
import ru.skilanov.spring.dto.GenreDto;
import ru.skilanov.spring.mapper.CommentMapperImpl;
import ru.skilanov.spring.mapper.AuthorMapperImpl;
import ru.skilanov.spring.mapper.GenreMapperImpl;
import ru.skilanov.spring.mapper.BookMapperImpl;
import ru.skilanov.spring.repositories.api.CommentRepository;
import ru.skilanov.spring.repositories.impl.AuthorRepositoryJpa;
import ru.skilanov.spring.repositories.impl.BookRepositoryJpa;
import ru.skilanov.spring.repositories.impl.CommentRepositoryJpa;
import ru.skilanov.spring.repositories.impl.GenreRepositoryJpa;
import ru.skilanov.spring.service.api.CommentService;
import ru.skilanov.spring.service.impl.AuthorServiceImpl;
import ru.skilanov.spring.service.impl.BookServiceImpl;
import ru.skilanov.spring.service.impl.CommentServiceImpl;
import ru.skilanov.spring.service.impl.GenreServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({CommentServiceImpl.class, BookServiceImpl.class,
        CommentRepositoryJpa.class, BookRepositoryJpa.class,
        AuthorServiceImpl.class, AuthorRepositoryJpa.class,
        GenreServiceImpl.class, GenreRepositoryJpa.class,
        AuthorMapperImpl.class, GenreMapperImpl.class,
        BookMapperImpl.class, CommentMapperImpl.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentServiceImplTest {

    public static final long ID = 1L;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    private CommentDto expectedComment;

    private CommentDto savedComment;

    private GenreDto genre;

    private AuthorDto author;

    private BookDto book;

    @BeforeEach
    public void setUp() {
        genre = new GenreDto(ID, "Genre_" + ID);
        author = new AuthorDto(ID, "Author_" + ID);
        book = new BookDto(ID, "Book_" + ID, author, genre);
        savedComment = new CommentDto(4L, "Comment_" + ID, book);
        expectedComment = new CommentDto(ID, "Comment_" + ID, book);
    }

    @Test
    public void whenFindCommentByIdThenItReturns() {
        var comment = commentService.findById(ID);

        Assertions.assertThat(comment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    public void whenFindAllCommentsThenAllFound() {
        var comment = getDbComments().get(0);
        comment.setBook(book);
        var expectedComments = Collections.singletonList(comment);

        var comments = commentService.findAllByBookId(ID);

        Assertions.assertThat(comments)
                .containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    @Test
    public void whenCreateCommentThenItInserted() {
        var resultComment = commentService.create("Comment_" + ID, ID);

        assertThat(resultComment).isEqualTo(savedComment);
    }

    @Test
    public void whenUpdateCommentThenItUpdated() {
        var updatedComment = new CommentDto(1, "Comment_" + 2, book);

        var resultComment = commentService.update(ID, "Comment_" + 2, ID);

        assertThat(resultComment).isEqualTo(updatedComment);
    }

    @Test
    public void whenDeleteCommentThenItDeleted() {
        Assertions.assertThat(commentService.findById(ID)).isPresent();
        commentService.deleteById(ID);
        Assertions.assertThat(commentService.findById(ID)).isNotPresent();
    }

    private static List<CommentDto> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> CommentDto.builder()
                        .id(id)
                        .description("Comment_" + id)
                        .build()
                )
                .toList();
    }
}
