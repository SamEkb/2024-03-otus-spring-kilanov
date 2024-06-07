package ru.skilanov.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.repositories.impl.BookRepositoryJpa;
import ru.skilanov.spring.repositories.impl.CommentRepositoryJpa;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import({CommentRepositoryJpa.class, BookRepositoryJpa.class})
public class CommentRepositoryJpaTest {

    public static final long ID = 1L;

    @Autowired
    private CommentRepositoryJpa repositoryJpa;

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void whenFindCommentByIdThenItReturns(Comment expectedComment) {
        expectedComment.setBook(bookRepositoryJpa.findById(expectedComment.getId()).orElse(null));
        var actualComment = repositoryJpa.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать все комментарии по id книги")
    @Test
    void whenFindAllCommentsByBookIdThenTheyReturn() {
        var actualComments = repositoryJpa.findAllByBookId(ID);
        var comment = getDbComments().get(0);
        comment.setBook(bookRepositoryJpa.findById(ID).orElse(null));
        var expectedComments = Collections.singletonList(comment);

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void whenSaveCommentThenItSaved() {
        var book = bookRepositoryJpa.findById(ID).orElse(null);
        var expectedComment = new Comment(0, "awesome", book);
        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void whenSaveUpdatedCommentThenItReturnsUpdated() {
        var book = bookRepositoryJpa.findById(ID).orElse(null);
        var actualComment = new Comment(1, "awful", book);
        var expectedComment = new Comment(1, "awesome", book);

        assertThat(repositoryJpa.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(actualComment);

        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void whenDeleteCommentThenItDeleted() {
        repositoryJpa.delete(ID);
        assertThat(repositoryJpa.findById(ID)).isEmpty();
    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> Comment.builder()
                        .id(id)
                        .description("Comment_" + id)
                        .build()
                )
                .toList();
    }
}
