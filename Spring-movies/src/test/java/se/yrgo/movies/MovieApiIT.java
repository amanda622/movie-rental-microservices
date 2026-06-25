package se.yrgo.movies;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Full integration test: the whole Spring Boot app on a random port + real H2,
 * driven over HTTP with REST Assured. Named *IT so Failsafe runs it in `verify`.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieApiIT {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createThenFetchMovie_roundTripsThroughHttpAndDb() {
        // create
        given().contentType(ContentType.JSON)
                .body("{\"movieTitle\":\"Blade Runner\",\"genre\":\"thriller\"}")
                .when().post("/movies/create-movie")
                .then().statusCode(201)
                .body("movieTitle", equalTo("Blade Runner"));

        // list
        given().when().get("/movies/get-all-movies")
                .then().statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void getMovieById_missing_returns404() {
        given().when().get("/movies/get-movie-by-id/999999")
                .then().statusCode(404);
    }
}
