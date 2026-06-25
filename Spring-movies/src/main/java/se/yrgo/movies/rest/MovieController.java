package se.yrgo.movies.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;
import se.yrgo.movies.domain.MovieSeries;
import se.yrgo.movies.service.MovieService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/get-all-movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/get-all-movie-series")
    public List<MovieSeries> getAllMovieSeries() {
        return movieService.getAllMovieSeries();
    }

    @GetMapping("/get-movie-by-id/{id}")
    public Movie getMovieById(@PathVariable long id) {
        return movieService.getMovie(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No movie with id " + id));
    }

    @GetMapping("/get-movies-by-genre/{genre}")
    public List<Movie> getMoviesByGenre(@PathVariable Genre.MovieGenre genre) {
        return movieService.getMoviesByGenre(genre);
    }

    @GetMapping("/get-genres")
    public List<Genre.MovieGenre> getAllMovieGenres() {
        return List.of(Genre.MovieGenre.values());
    }

    @PostMapping("/create-movie")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @PostMapping("/create-movie-series")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieSeries createMovieSeries(@RequestBody MovieSeries movieSeries) {
        return movieService.createMovieSeries(movieSeries);
    }
}
