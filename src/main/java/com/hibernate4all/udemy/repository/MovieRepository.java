package com.hibernate4all.udemy.repository;

import com.hibernate4all.udemy.domain.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void persist(Movie movie) {
        entityManager.persist(movie);
    }

    public List<Movie> getAll() {
        throw new UnsupportedOperationException();
    }
}
