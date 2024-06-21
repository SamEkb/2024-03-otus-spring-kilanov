package ru.skilanov.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skilanov.spring.models.Comment;
import ru.skilanov.spring.repositories.BookRepository;
import ru.skilanov.spring.repositories.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Comments repository test")
@DataMongoTest
@Testcontainers
public class CommentRepositoryTest {

    private static final String EXPECTED_DESCRIPTION_ONE = "cool";

    private static final String EXPECTED_ID = "3";

    private static final String DEFAULT_ID_ONE = "1";

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @DisplayName("Add comment to db")
    @Test
    void shouldInsertComment() {
        var book = bookRepository.findById(DEFAULT_ID_ONE);
        var expectedComment = Comment.builder()
                        .description(EXPECTED_DESCRIPTION_ONE)
                                .book(book.orElse(null))
                                        .build();
        commentRepository.save(expectedComment);
        Optional<Comment> actualComment = commentRepository.findById(EXPECTED_ID);
        actualComment.ifPresent(it -> assertThat(it.getDescription()).isEqualTo(expectedComment.getDescription()));
    }

    @DisplayName("Returns expected comment")
    @Test
    void shouldReturnExpectedCommentById() {
        var book = bookRepository.findById(DEFAULT_ID_ONE);
        var expectedComment = new Comment(DEFAULT_ID_ONE, EXPECTED_DESCRIPTION_ONE, book.orElse(null));
        Optional<Comment> actualComment = commentRepository.findById(expectedComment.getId());
        actualComment.ifPresent(it -> assertThat(it).usingRecursiveComparison().isEqualTo(expectedComment));
    }

    @DisplayName("Deletes expected comment by id")
    @Test
    void shouldCorrectDeleteCommentById() {
        Comment commentForDeleting = commentRepository.findAll().get(0);
        commentRepository.delete(commentForDeleting);

        Comment deletedComment = commentRepository.findById(commentForDeleting.getId())
                .orElse(null);

        assertThat(deletedComment).isNull();
    }
}
