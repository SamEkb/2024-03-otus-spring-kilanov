package ru.skilanov.spring.repositories;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcAuthorRepository(JdbcOperations jdbc, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = jdbc;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> findAll() {
        return jdbc.query("SELECT id, full_name FROM authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        return namedParameterJdbcOperations.query(
                "SELECT id, full_name FROM authors WHERE id = :id",
                params,
                new AuthorRowMapper()
        ).stream().findFirst();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            return insert(author);
        }
        return update(author);
    }

    private Author insert(Author author) {
        var keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcOperations.update(
                "INSERT INTO authors (full_name) VALUES (:fullName)",
                new MapSqlParameterSource()
                        .addValue("fullName", author.getFullName()),
                keyHolder,
                new String[]{"id"}
        );

        //noinspection DataFlowIssue
        author.setId(keyHolder.getKeyAs(Long.class));
        return author;
    }

    private Author update(Author author) {
        int result = namedParameterJdbcOperations.update(
                "update authors set full_name = :fullName where id = :id",
                Map.of("id", author.getId(), "fullName", author.getFullName())
        );
        if (result <= 0) {
            throw new EntityNotFoundException("No authors were updated");
        }
        return author;
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from authors where id = :id", Map.of("id", id));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
