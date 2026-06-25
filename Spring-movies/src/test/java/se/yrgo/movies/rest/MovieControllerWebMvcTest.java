package se.yrgo.movies.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;
import se.yrgo.movies.service.MovieService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web-slice test: only the MVC layer is loaded, the service is mocked.
 * Verifies HTTP semantics — status codes, JSON shape, routing.
 */
@WebMvcTest(MovieController.class)
class MovieControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    void getAllMovies_returns200AndJsonArray() throws Exception {
        when(movieService.getAllMovies()).thenReturn(List.of(new Movie(), new Movie()));

        mockMvc.perform(get("/movies/get-all-movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getMovieById_missing_returns404() throws Exception {
        when(movieService.getMovie(42L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/movies/get-movie-by-id/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGenres_returnsAllEnumValues() throws Exception {
        mockMvc.perform(get("/movies/get-genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(Genre.MovieGenre.values().length));
    }

    @Test
    void createMovie_returns201() throws Exception {
        when(movieService.createMovie(any(Movie.class))).thenReturn(new Movie());

        mockMvc.perform(post("/movies/create-movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movieTitle\":\"Dune\",\"genre\":\"action\"}"))
                .andExpect(status().isCreated());
    }
}
