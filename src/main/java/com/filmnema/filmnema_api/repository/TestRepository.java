package com.filmnema.filmnema_api.repository;

import com.filmnema.filmnema_api.model.Movie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepository {

    private static final String FIND_FIRST_MOVIE_SQL = """
            SELECT id,
                   created_date,
                   modified_date,
                   available_globally,
                   locale,
                   original_title,
                   release_date,
                   runtime,
                   title
            FROM movie
            ORDER BY id
            LIMIT 1
            """;

    private final JdbcTemplate jdbcTemplate;

    public TestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Movie> findFirstMovie() {
        return jdbcTemplate.query(FIND_FIRST_MOVIE_SQL, this::mapMovie).stream().findFirst();
    }

    private Movie mapMovie(ResultSet resultSet, int rowNum) throws SQLException {
        return new Movie(
                resultSet.getLong("id"),
                resultSet.getObject("created_date", java.time.OffsetDateTime.class),
                resultSet.getObject("modified_date", java.time.OffsetDateTime.class),
                resultSet.getObject("available_globally", Boolean.class),
                resultSet.getString("locale"),
                resultSet.getString("original_title"),
                resultSet.getObject("release_date", java.time.LocalDate.class),
                resultSet.getObject("runtime", Long.class),
                resultSet.getString("title")
        );
    }
}