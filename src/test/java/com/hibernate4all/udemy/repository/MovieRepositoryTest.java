package com.hibernate4all.udemy.repository;

import com.hibernate4all.udemy.config.PersistenceConfig;
import com.hibernate4all.udemy.domain.Movie;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
@SqlConfig(dataSource = "dataSourceH2", transactionManager = "transactionManager")
@Sql({"/data/datas-test.sql"})
class MovieRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepositoryTest.class);

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void save_casNominal() {
        Movie movie = Movie.builder().name("Inception").build();
        movieRepository.persist(movie);
        assertTrue(movie.getId() > 0, "L'ID du film doit être supérieur à 0 après la persistance.");
    }

    @Test
    void find_casNominal() {
        Movie memento = movieRepository.find(-2L);
        assertEquals("Memento", memento.getName(), () -> "Mauvais film récupéré");
    }

    @Test
    void getAll() {
        List<Movie> movies = movieRepository.getAll();
        assertEquals(2, movies.size());
    }

    @Test
    public void merge_cas_simule() {
        Movie movie = Movie.builder().name("Inception 2").id(-1L).build();
        Movie movieMerged = movieRepository.merge(movie);
        assertEquals(movieMerged.getName(), "Inception 2", () -> "Le nom du film n'a pas été mis à jour");
    }

    @Test
    void remove() {
        movieRepository.remove(-2L);
    }

    @Test
    void getReference_cas_Nominal() {
        Movie movie = movieRepository.getReference(-2L);
        assertEquals(movie.getId(), -2L, () -> "La réf n'a pas été chargée correctement");
    }

    @Test
    void getReference_failure() {
        Movie movie = movieRepository.getReference(-2L);
        assertThrows(LazyInitializationException.class, () -> {
            LOGGER.trace("movie name : {}", movie.getName());
            assertEquals(movie.getId(), -2L, () -> "La réf n'a pas été chargée correctement");
        });
    }
}