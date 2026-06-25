package se.yrgo.movies.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Persistence-slice test: spins up an in-memory H2 + JPA only and exercises the
 * custom @Query against a real database.
 */
@DataJpaTest
class MoviesRepositoryDataJpaTest {

    @Autowired
    private MoviesRepository moviesRepository;

    @Test
    void findMovieByGenre_returnsOnlyMatchingGenre() {
        moviesRepository.save(movie("Alien", Genre.MovieGenre.thriller));
        moviesRepository.save(movie("Dune", Genre.MovieGenre.action));
        moviesRepository.save(movie("Predator", Genre.MovieGenre.action));

        assertThat(moviesRepository.findMovieByGenre(Genre.MovieGenre.action))
                .extracting(Movie::getMovieTitle)
                .containsExactlyInAnyOrder("Dune", "Predator");
    }

    private Movie movie(String title, Genre.MovieGenre genre) {
        Movie m = new Movie();
        m.setMovieTitle(title);
        m.setGenre(genre);
        return m;
    }
}
