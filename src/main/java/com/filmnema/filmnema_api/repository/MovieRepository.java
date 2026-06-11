package com.filmnema.filmnema_api.repository;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.filmnema.filmnema_api.dto.MovieCreateRequest;
import com.filmnema.filmnema_api.model.Movie;

@Repository
public class MovieRepository implements IMovieRepository {

    private final JdbcTemplate jdbcTemplate;

    public MovieRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Movie> findFirstMovie() {
        final String querySql = """
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

        return jdbcTemplate.query(querySql, this::mapMovie).stream().findFirst();
    }

    @Override
    public Optional<Movie> findMovieById(Long id) {
        final String querySql = """
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
                WHERE id = ?
                LIMIT 1
                """;

        return jdbcTemplate.query(querySql, this::mapMovie, id).stream().findFirst();
    }

    @Override
    public List<Movie> findMovies(String title, int offset, int size) {
        StringBuilder querySql = new StringBuilder("""
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
                """);

        List<Object> params = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            querySql.append(" WHERE title ILIKE ?");
            params.add("%" + title.trim() + "%");
        }

        querySql.append(" ORDER BY id LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        return jdbcTemplate.query(querySql.toString(), this::mapMovie, params.toArray());
    }

    @Override
    public long countMovies(String title) {
        StringBuilder querySql = new StringBuilder("SELECT COUNT(*) FROM movie");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            querySql.append(" WHERE title ILIKE ?");
            params.add("%" + title.trim() + "%");
        }

        Long count = jdbcTemplate.queryForObject(querySql.toString(), Long.class, params.toArray());
        return count == null ? 0L : count;
    }

    @Override
    public Movie createMovie(MovieCreateRequest request) {
        final String insertSql = """
                INSERT INTO movie (
                    created_date,
                    modified_date,
                    available_globally,
                    locale,
                    original_title,
                    release_date,
                    runtime,
                    title
                )
                VALUES (
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP,
                    ?, ?, ?, ?, ?, ?
                )
                RETURNING id,
                          created_date,
                          modified_date,
                          available_globally,
                          locale,
                          original_title,
                          release_date,
                          runtime,
                          title
                """;

        return jdbcTemplate.queryForObject(
                insertSql,
                this::mapMovie,
                request.availableGlobally(),
                request.locale(),
                request.originalTitle(),
                request.releaseDate(),
                request.runtime(),
                request.title()
        );
    }

    @Override
    public int updateMovie(Long id, Boolean availableGlobally, String locale, String originalTitle, java.time.LocalDate releaseDate, Long runtime, String title) {
        final String updateSql = """
                UPDATE movie
                SET available_globally = ?,
                    locale = ?,
                    original_title = ?,
                    release_date = ?,
                    runtime = ?,
                    title = ?,
                    modified_date = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                updateSql,
                availableGlobally,
                locale,
                originalTitle,
                releaseDate,
                runtime,
                title,
                id
        );
    }

    @Override
    public int deleteMovie(Long id) {
        final String deleteSql = "DELETE FROM movie WHERE id = ?";
        return jdbcTemplate.update(deleteSql, id);
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
                resultSet.getString("title"));
    }

}
