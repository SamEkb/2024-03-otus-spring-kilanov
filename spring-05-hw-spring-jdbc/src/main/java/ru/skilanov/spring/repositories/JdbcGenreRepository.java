package ru.skilanov.spring.repositories;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.skilanov.spring.exception.EntityNotFoundException;
import ru.skilanov.spring.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcGenreRepository(JdbcOperations jdbc, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = jdbc;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query("SELECT id, name FROM genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        return namedParameterJdbcOperations.query(
                "SELECT id, name FROM genres WHERE id = :id",
                params,
                new GenreRowMapper()
        ).stream().findFirst();
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            return insert(genre);
        }
        return update(genre);
    }

    private Genre insert(Genre genre) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());

        namedParameterJdbcOperations.update(
                """
                        INSERT INTO genres (name) VALUES (:name)
                        """, params, keyHolder, new String[]{"id"}
        );

        //noinspection DataFlowIssue
        genre.setId(keyHolder.getKeyAs(Long.class));
        return genre;
    }

    private Genre update(Genre genre) {
        int result = namedParameterJdbcOperations.update(
                "update genres set name = :name where id = :id",
                Map.of("id", genre.getId(), "name", genre.getName())
        );
        if (result <= 0) {
            throw new EntityNotFoundException("No genres were updated");
        }
        return genre;
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from genres where id = :id", Map.of("id", id));
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long genreId = rs.getLong("id");
            String genreName = rs.getString("name");

            return new Genre(genreId, genreName);
        }
    }
}
