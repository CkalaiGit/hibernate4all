package com.hibernate4all.udemy.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibernate4all.udemy.domain.Movie;
import com.hibernate4all.udemy.repository.IMovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getAllMovies() testing : should return 200 status code")
    void shouldReturnAllMoviesInAscendingOrderOfId() throws Exception {
        // Arrange
        Movie movie1 = Movie.builder().id(1L).name("movie name 1").build();
        Movie movie2 = Movie.builder().id(2L).name("movie name 2").build();

        List<Movie> movies = List.of(movie1, movie2);
        when(movieRepository.getAll()).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    @DisplayName("createMovie() testing : should Return 201 status code")
    public void createMovie_ShouldReturn201_WhenMovieIsCreated() throws Exception {
        Movie movie = new Movie(1L, "Sonic");
        doNothing().when(movieRepository).persist(any(Movie.class));
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/movies/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sonic"));
    }

    @Test
    @DisplayName("createMovie() testing : should Return 400 status code")
    public void createMovie_ShouldReturn400_WhenMovieIsCreated() throws Exception {
        Movie movie = new Movie(1L, "");
        doNothing().when(movieRepository).persist(any(Movie.class));
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isBadRequest());

        assertThrows(IllegalArgumentException.class, () -> {
            new MovieController(movieRepository).createMovie(movie);
        });
    }

    @Test
    @DisplayName("findMovieById() testing : should Return 404 status code")
    public void findMovieById_ShouldReturn404_case() throws Exception {
        when(movieRepository.find(103L)).thenReturn(null);
        mockMvc.perform(get("/movies/103"))
                .andExpect(status().isNotFound());

        assertThrows(NoSuchElementException.class, () -> {
            new MovieController(movieRepository).findMovieById(103L);
        });

    }

    @Test
    @DisplayName("findMovieById() testing : should Return 200 status code")
    public void findMovieById_ShouldReturn200_case() throws Exception {
        when(movieRepository.find(1L)).thenReturn(new Movie(1L, "Sonic"));
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sonic"));
    }

    @Test
    @DisplayName("updateMovie() testing : should Return 404 status code")
    void updateMovie_WithInvalidData_ShouldReturn404() throws Exception {
        Movie updatedMovie = new Movie(1L, "LOL");
        when(movieRepository.find(anyLong())).thenReturn(null);
        mockMvc.perform(put("/movies/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMovie)))
                        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateMovie() testing : should Return 200 status code")
    void updateMovie_WhenMovieExist_WithANewTitle_ShouldReturn200() throws Exception {
        Movie existingMovie = new Movie(1L, "Existing Title");
        Movie updatedMovie = new Movie(1L, "New Title");

        when(movieRepository.find(anyLong())).thenReturn(existingMovie);
        doNothing().when(movieRepository).persist(any(Movie.class));


        mockMvc.perform(put("/movies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingMovie)))
                        .andExpect(status().isOk());

    }

    @Test
    @DisplayName("deleteTodoById() testing : should Return 204 status code")
    void testDeleteTodoById() throws Exception {
        doNothing().when(movieRepository).remove(1L);
        // Act & Assert
        mockMvc.perform(delete("/movies/{id}", 1L))
                .andExpect(status().isNoContent());
        // Verify
        verify(movieRepository, times(1)).remove(1L);
    }



}