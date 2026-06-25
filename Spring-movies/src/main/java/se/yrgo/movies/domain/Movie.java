package se.yrgo.movies.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String movieTitle;
    private LocalDate releaseDate;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre.MovieGenre genre;

    @ManyToOne(cascade = CascadeType.MERGE)
    private MovieSeries movieSeries;

    public Movie() {
    }

    public Movie(Long id, String movieTitle, LocalDate releaseDate, String description,
                 Genre.MovieGenre genre, MovieSeries movieSeries) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.description = description;
        this.genre = genre;
        this.movieSeries = movieSeries;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre.MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(Genre.MovieGenre genre) {
        this.genre = genre;
    }

    public MovieSeries getMovieSeries() {
        return movieSeries;
    }

    public void setMovieSeries(MovieSeries movieSeries) {
        this.movieSeries = movieSeries;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieTitle='" + movieTitle + '\'' +
                ", releaseDate=" + releaseDate +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
