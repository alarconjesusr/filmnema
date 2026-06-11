package com.filmnema.filmnema_api.controller;

import com.filmnema.filmnema_api.dto.MovieCreateRequest;
import com.filmnema.filmnema_api.dto.MovieUpdateRequest;
import com.filmnema.filmnema_api.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filmnema.filmnema_api.model.Movie;
import com.filmnema.filmnema_api.service.IMovieService;


@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final IMovieService movieService;

    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("first")
    public ResponseEntity<Movie> getFirstMovie() {
        return movieService.getFirstMovie()
                .map(movie -> ResponseEntity.ok().body(movie))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<PageResponse<Movie>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(movieService.searchMovies(title, page, size));
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieCreateRequest request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody MovieUpdateRequest request) {
        return movieService.updateMovie(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (movieService.deleteMovie(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
