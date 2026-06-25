package se.yrgo.movies.service;

import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;
import se.yrgo.movies.domain.MovieSeries;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAllMovies();

    Movie createMovie(Movie movie);

    Optional<Movie> getMovie(Long id);

    List<Movie> getMoviesByGenre(Genre.MovieGenre genre);

    List<MovieSeries> getAllMovieSeries();

    MovieSeries createMovieSeries(MovieSeries movieSeries);
}
