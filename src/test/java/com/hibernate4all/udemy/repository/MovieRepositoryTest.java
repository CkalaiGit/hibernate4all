package com.hibernate4all.udemy.repository;

import com.hibernate4all.udemy.config.PersistenceConfig;
import com.hibernate4all.udemy.domain.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceConfig.class})
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void save_casNominal() {
        Movie movie = Movie.builder().name("Inception").build();
        movieRepository.persist(movie);
        assertTrue(movie.getId() > 0, "L'ID du film doit être supérieur à 0 après la persistance.");
    }

    @Test
    void getAll() {
    }
}