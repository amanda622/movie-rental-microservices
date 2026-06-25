package se.yrgo.movies.service;

import org.springframework.stereotype.Service;
import se.yrgo.movies.data.MovieSeriesRepository;
import se.yrgo.movies.data.MoviesRepository;
import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;
import se.yrgo.movies.domain.MovieSeries;

import java.util.List;
import java.util.Optional;

/**
 * Single, idiomatic implementation of {@link MovieService}.
 * <p>
 * The legacy project shipped two beans ({@code MovieServiceImpl} and
 * {@code MovieServiceImpl2}) implementing the same interface, the second wrongly
 * annotated {@code @Repository}, forcing a brittle {@code @Qualifier} in the
 * controller. We keep one Spring-Data-backed bean so wiring is unambiguous.
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MoviesRepository moviesRepository;
    private final MovieSeriesRepository movieSeriesRepository;

    public MovieServiceImpl(MoviesRepository moviesRepository,
                            MovieSeriesRepository movieSeriesRepository) {
        this.moviesRepository = moviesRepository;
        this.movieSeriesRepository = movieSeriesRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return moviesRepository.findAll();
    }

    @Override
    public Movie createMovie(Movie movie) {
        return moviesRepository.save(movie);
    }

    @Override
    public Optional<Movie> getMovie(Long id) {
        return moviesRepository.findById(id);
    }

    @Override
    public List<Movie> getMoviesByGenre(Genre.MovieGenre genre) {
        return moviesRepository.findMovieByGenre(genre);
    }

    @Override
    public List<MovieSeries> getAllMovieSeries() {
        // Legacy bug: this returned null, so the controller's isEmpty() check threw NPE.
        return movieSeriesRepository.findAll();
    }

    @Override
    public MovieSeries createMovieSeries(MovieSeries movieSeries) {
        return movieSeriesRepository.save(movieSeries);
    }
}
