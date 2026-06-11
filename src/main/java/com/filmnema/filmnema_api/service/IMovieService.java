package com.filmnema.filmnema_api.service;

import java.util.Optional;

import com.filmnema.filmnema_api.dto.MovieCreateRequest;
import com.filmnema.filmnema_api.dto.MovieUpdateRequest;
import com.filmnema.filmnema_api.dto.PageResponse;
import com.filmnema.filmnema_api.model.Movie;

public interface IMovieService {
    public Optional<Movie> getFirstMovie();
    public Optional<Movie> getMovieById(Long id);
    public PageResponse<Movie> searchMovies(String title, int page, int size);
    public Movie createMovie(MovieCreateRequest request);
    public Optional<Movie> updateMovie(Long id, MovieUpdateRequest request);
    public boolean deleteMovie(Long id);
}
