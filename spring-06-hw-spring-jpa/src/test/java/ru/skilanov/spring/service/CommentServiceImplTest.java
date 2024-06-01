package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.repositories.api.CommentRepository;
import ru.skilanov.spring.service.impl.BookServiceImpl;
import ru.skilanov.spring.service.impl.CommentServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CommentServiceImpl.class})
public class CommentServiceImplTest {

    public static final long ID = 1L;

    @Autowired
    private CommentServiceImpl commentService;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private CommentRepository commentRepository;

    private Comment expectedComment;

    @BeforeEach
    public void setUp() {
        expectedComment = new Comment(ID, "Comment_" + ID, new Book());
    }

    @Test
    public void whenFindCommentByIdThenItReturns() {
        when(commentRepository.findById(ID)).thenReturn(Optional.of(expectedComment));

        var comment = commentService.findById(ID);

        verify(commentRepository, times(1)).findById(ID);

        assertThat(comment).isEqualTo(Optional.of(expectedComment));
    }

    @Test
    public void whenFindAllCommentsThenAllFound() {
        var expectedComments = Collections.singletonList(getDbComments().get(0));
        when(commentRepository.findAllByBookId(ID)).thenReturn(expectedComments);

        var comments = commentService.findAllByBookId(ID);

        verify(commentRepository, times(1)).findAllByBookId(ID);

        Assertions.assertThat(comments)
                .containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    @Test
    public void whenInsertCommentThenItInserted() {
        var book = new Book();
        when(bookService.findById(ID)).thenReturn(Optional.of(book));
        var newComment = new Comment(0, "Comment_" + ID, book);
        when(commentRepository.save(newComment)).thenReturn(expectedComment);

        var resultComment = commentService.insert("Comment_" + ID, ID);

        verify(commentRepository, times(1)).save(newComment);

        assertThat(resultComment).isEqualTo(expectedComment);
    }

    @Test
    public void whenUpdateCommentThenItUpdated() {
        var updatedComment = new Comment(1, "Comment_" + 2, new Book());
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);

        var resultComment = commentService.update(ID, "Comment_" + 2);

        verify(commentRepository, times(1)).save(updatedComment);

        assertThat(resultComment).isEqualTo(updatedComment);
    }

    @Test
    public void whenDeleteCommentThenItDeleted() {
        when(commentRepository.findById(ID)).thenReturn(Optional.of(expectedComment));
        doNothing().when(commentRepository).delete(expectedComment);
        commentService.deleteById(ID);
        verify(commentRepository, times(1)).delete(expectedComment);
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
