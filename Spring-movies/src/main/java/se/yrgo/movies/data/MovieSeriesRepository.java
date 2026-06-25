package se.yrgo.movies.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.yrgo.movies.domain.MovieSeries;

@Repository
public interface MovieSeriesRepository extends JpaRepository<MovieSeries, Long> {
}
