package com.hibernate4all.udemy.web;

import com.hibernate4all.udemy.domain.Movie;
import com.hibernate4all.udemy.repository.IMovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/movies")
@AllArgsConstructor
public class MovieController {

    private final IMovieRepository movieRepository;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.getAll());
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        if (movie.getName() == null || movie.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        movieRepository.persist(movie);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getId())
                .toUri();

        return ResponseEntity.created(location).body(movie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findMovieById(@PathVariable long id) {
        Movie movie = Optional.ofNullable(movieRepository.find(id))
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable long id, @RequestBody Movie movie2update) {
        if (movie2update.getName() == null || movie2update.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        Optional<Movie> existingMovie = Optional.ofNullable(movieRepository.find(id));
        if (existingMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie updatedMovie = new Movie(id, movie2update.getName());
        movieRepository.persist(updatedMovie);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable long id) {
        movieRepository.remove(id);
        return ResponseEntity.noContent().build();
    }

}
