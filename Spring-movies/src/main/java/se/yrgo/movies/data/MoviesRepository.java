package se.yrgo.movies.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.yrgo.movies.domain.Genre;
import se.yrgo.movies.domain.Movie;

import java.util.List;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m WHERE m.genre = :genre")
    List<Movie> findMovieByGenre(@Param("genre") Genre.MovieGenre genre);
}
