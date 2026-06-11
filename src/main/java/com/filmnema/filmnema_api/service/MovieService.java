package com.filmnema.filmnema_api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.filmnema.filmnema_api.dto.MovieCreateRequest;
import com.filmnema.filmnema_api.dto.MovieUpdateRequest;
import com.filmnema.filmnema_api.dto.PageResponse;
import com.filmnema.filmnema_api.exception.MovieRequestException;
import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.repository.IMovieRepository;

@Service
public class MovieService implements IMovieService {

    private static final int MAX_PAGE_SIZE = 100;

    private final IMovieRepository movieRepository;

    public MovieService(IMovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Optional<Movie> getFirstMovie() {
        return movieRepository.findFirstMovie();
    }

    @Override
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findMovieById(id);
    }

    @Override
    public PageResponse<Movie> searchMovies(String title, int page, int size) {
        if (page < 0) {
            throw new MovieRequestException("Page must be greater than or equal to 0.");
        }

        if (size < 1) {
            throw new MovieRequestException("Size must be greater than 0.");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new MovieRequestException("Size must not be greater than " + MAX_PAGE_SIZE + ".");
        }

        int safePage = page;
        int safeSize = size;
        int offset = safePage * safeSize;
        long totalElements = movieRepository.countMovies(title);
        int totalPages = (int) Math.ceil((double) totalElements / safeSize);

        return new PageResponse<>(
                movieRepository.findMovies(title, offset, safeSize),
                safePage,
                safeSize,
                totalElements,
                totalPages
        );
    }

    @Override
    public Movie createMovie(MovieCreateRequest request) {
        return movieRepository.createMovie(request);
    }

    @Override
    public Optional<Movie> updateMovie(Long id, MovieUpdateRequest request) {
        int updatedRows = movieRepository.updateMovie(
            id,
            request.availableGlobally(),
            request.locale(),
            request.originalTitle(),
            request.releaseDate(),
            request.runtime(),
            request.title()
        );

        if (updatedRows == 0) {
            return Optional.empty();
        }

        return movieRepository.findMovieById(id);
    }

    @Override
    public boolean deleteMovie(Long id) {
        return movieRepository.deleteMovie(id) > 0;
    }

}
