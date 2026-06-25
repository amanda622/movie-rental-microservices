package se.yrgo.movies.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.movies.data.MovieSeriesRepository;
import se.yrgo.movies.data.MoviesRepository;
import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pure unit test of the service layer — no Spring context, repositories mocked.
 * This is the fast base of the test pyramid.
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MoviesRepository moviesRepository;
    @Mock
    private MovieSeriesRepository movieSeriesRepository;
    @InjectMocks
    private MovieServiceImpl service;

    @Test
    void getAllMovies_returnsRepositoryContents() {
        when(moviesRepository.findAll()).thenReturn(List.of(new Movie(), new Movie()));

        assertThat(service.getAllMovies()).hasSize(2);
        verify(moviesRepository).findAll();
    }

    @Test
    void getAllMovieSeries_doesNotReturnNull() {
        // Locks the legacy bug: the old impl returned null here.
        when(movieSeriesRepository.findAll()).thenReturn(List.of());

        assertThat(service.getAllMovieSeries()).isNotNull().isEmpty();
    }

    @Test
    void getMoviesByGenre_delegatesToCustomQuery() {
        when(moviesRepository.findMovieByGenre(Genre.MovieGenre.action))
                .thenReturn(List.of(new Movie()));

        assertThat(service.getMoviesByGenre(Genre.MovieGenre.action)).hasSize(1);
        verify(moviesRepository).findMovieByGenre(Genre.MovieGenre.action);
    }

    @Test
    void getMovie_missingId_returnsEmptyOptional() {
        when(moviesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.getMovie(99L)).isEmpty();
    }
}
