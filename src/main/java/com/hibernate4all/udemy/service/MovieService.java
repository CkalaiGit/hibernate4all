package com.hibernate4all.udemy.service;

import com.hibernate4all.udemy.domain.Movie;
import com.hibernate4all.udemy.repository.IMovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieService {

    private final IMovieRepository movieRepository;

    public void updateDescription(long id, String description) {
        Movie movie = movieRepository.find(id);
    }

}
