package com.filmnema.filmnema_api.repository;

import java.util.List;
import java.util.Optional;

import com.filmnema.filmnema_api.model.Movie;

public interface IMovieRepository {
    public Optional<Movie> findFirstMovie();
    public Optional<Movie> findMovieById(Long id);
    public List<Movie> findMovies(String title, int offset, int size);
    public long countMovies(String title);
    public Movie createMovie(com.filmnema.filmnema_api.dto.MovieCreateRequest request);
    public int updateMovie(Long id, Boolean availableGlobally, String locale, String originalTitle, java.time.LocalDate releaseDate, Long runtime, String title);
    public int deleteMovie(Long id);
}
