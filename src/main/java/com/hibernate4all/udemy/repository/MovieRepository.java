package com.hibernate4all.udemy.repository;

import com.hibernate4all.udemy.domain.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepository implements IMovieRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void persist(Movie movie) {
        entityManager.persist(movie);
    }

    @Override
    public Movie find(Long id) {
        Movie result = entityManager.find(Movie.class, id);
        LOGGER.trace("entityManager.contains(result) : {} ", entityManager.contains(result));
        return result;
    }

    public List<Movie> getAll() {
        return entityManager.createQuery("from Movie", Movie.class).getResultList();
    }

    @Override
    @Transactional
    public Movie merge(Movie movie) {
        entityManager.merge(movie);
        return movie;
    }

    @Override
    @Transactional
    public void remove(Long l) {
        Movie movie = entityManager.find(Movie.class, l);
        entityManager.remove(movie);
    }

    @Override
    public Movie getReference(Long l) {
        return entityManager.getReference(Movie.class, l);
    }

}
