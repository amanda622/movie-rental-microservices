package se.yrgo.movies.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MovieSeries")
public class MovieSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String movieSeries;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movieSeries")
    private Set<Movie> movies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieSeries() {
        return movieSeries;
    }

    public void setMovieSeries(String movieSeries) {
        this.movieSeries = movieSeries;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
