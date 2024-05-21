package ru.skilanov.spring.repositories;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Author;
import ru.skilanov.spring.models.Book;
import ru.skilanov.spring.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(JdbcOperations jdbc, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = jdbc;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        return namedParameterJdbcOperations.query("""
                SELECT b.id AS bookId, b.title AS bookTitle,
                a.id AS authorId, a.full_name AS authorName,
                g.id AS genreId, g.name AS genreName 
                FROM books b
                JOIN authors a ON a.id = b.author_id 
                JOIN genres g ON g.id = b.genre_id
                WHERE b.id = :id
                """, params, new BookRowMapper()
        ).stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("""
                SELECT b.id AS bookId, b.title AS bookTitle,
                a.id AS authorId, a.full_name AS authorName,
                g.id AS genreId, g.name AS genreName 
                FROM books b
                JOIN authors a ON a.id = b.author_id 
                JOIN genres g ON g.id = b.genre_id
                """, new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from books where id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update("""
                INSERT INTO books (title, author_id, genre_id)
                                VALUES (:title, :author_id, :genre_id)
                """, params, keyHolder, new String[]{"id"}
        );

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int result = namedParameterJdbcOperations.update("""
                        update books set id = :id, title = :title, author_id = :author_id, genre_id = :genre_id 
                        where id = :id
                        """,
                Map.of("id", book.getId(), "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(), "genre_id", book.getGenre().getId())
        );
        if (result <= 0) {
            throw new EntityNotFoundException("No books were updated");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long authorId = rs.getLong("authorId");
            String fullName = rs.getString("authorName");
            var author = new Author(authorId, fullName);

            long genreId = rs.getLong("genreId");
            String genreName = rs.getString("genreName");
            var genre = new Genre(genreId, genreName);

            long id = rs.getLong("bookId");
            String title = rs.getString("bookTitle");
            return new Book(id, title, author, genre);
        }
    }
}
