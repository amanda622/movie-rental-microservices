package se.yrgo.e2e;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * End-to-end test across all three running microservices. Drives only public HTTP
 * APIs against the live docker compose stack (no internal classes), proving the
 * services actually talk to each other:
 *   create customer (customer-service)
 *   -> create movie (movies-service)
 *   -> create rental referencing them (rental-service, which validates the
 *      customer by calling customer-service)
 *   -> fetch the rental back.
 *
 * Base URLs default to the compose-mapped localhost ports and can be overridden
 * with -Dcustomer.url / -Dmovies.url / -Drental.url. Run with:
 *   docker compose up -d --build
 *   ./mvnw -Pe2e -pl e2e-tests verify
 */
class MovieRentalFlowIT {

    private static final String CUSTOMER = System.getProperty("customer.url", "http://localhost:8080");
    private static final String MOVIES = System.getProperty("movies.url", "http://localhost:8081");
    private static final String RENTAL = System.getProperty("rental.url", "http://localhost:8082");

    @Test
    void fullRentalJourneyAcrossServices() {
        // 1. Create a customer
        int customerId = given().baseUri(CUSTOMER).contentType(ContentType.JSON)
                .body("{\"name\":\"Ada Lovelace\",\"email\":\"ada@example.com\"}")
                .when().post("/customer/create-customer")
                .then().statusCode(201)
                .extract().path("id");

        // 2. Create a movie
        int movieId = given().baseUri(MOVIES).contentType(ContentType.JSON)
                .body("{\"movieTitle\":\"The Matrix\",\"genre\":\"action\"}")
                .when().post("/movies/create-movie")
                .then().statusCode(201)
                .extract().path("id");

        // 3. Create a rental that references both (rental-service calls customer-service)
        int rentalId = given().baseUri(RENTAL).contentType(ContentType.JSON)
                .body("{\"customerId\":" + customerId + ",\"movieId\":" + movieId
                        + ",\"rentalCost\":80,\"rentalDate\":\"2024-05-01\"}")
                .when().post("/rental/create-rental")
                .then().statusCode(201)
                .extract().path("id");

        // 4. Read it back
        given().baseUri(RENTAL)
                .when().get("/rental/findById/" + rentalId)
                .then().statusCode(200)
                .body("customerId", equalTo(customerId))
                .body("movieId", equalTo(movieId));
    }

    @Test
    void rentalWithUnknownCustomerIsRejected() {
        given().baseUri(RENTAL).contentType(ContentType.JSON)
                .body("{\"customerId\":999999,\"movieId\":1,\"rentalCost\":50}")
                .when().post("/rental/create-rental")
                .then().statusCode(400);
    }
}
