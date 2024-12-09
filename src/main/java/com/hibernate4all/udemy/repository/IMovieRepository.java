package com.hibernate4all.udemy.repository;

import com.hibernate4all.udemy.domain.Movie;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IMovieRepository {

    void persist(Movie movie);

    Movie find(Long id);

    Movie merge(Movie movie);

    void remove(Long l);

    Movie getReference(Long l);

    List<Movie> getAll();
}
