package se.yrgo.rental;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;

/**
 * Full integration test of rental-service over HTTP + real H2, with the
 * downstream customer-service replaced by a WireMock stub. Exercises the
 * microservice boundary: a known customer -> 201, an unknown customer -> 400.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentalApiIT {

    static WireMockServer customerService = new WireMockServer(options().dynamicPort());

    static {
        customerService.start();
    }

    @DynamicPropertySource
    static void customerServiceUrl(DynamicPropertyRegistry registry) {
        registry.add("customer.service.url", () -> "http://localhost:" + customerService.port());
    }

    @AfterAll
    static void stopWireMock() {
        customerService.stop();
    }

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        customerService.resetAll();
    }

    @Test
    void createRental_knownCustomer_returns201() {
        customerService.stubFor(get(urlEqualTo("/customer/get-customer-by-id/1"))
                .willReturn(okJson("{\"id\":1}")));

        given().contentType(ContentType.JSON)
                .body("{\"customerId\":1,\"movieId\":2,\"rentalCost\":75}")
                .when().post("/rental/create-rental")
                .then().statusCode(201);
    }

    @Test
    void createRental_unknownCustomer_returns400() {
        customerService.stubFor(get(urlEqualTo("/customer/get-customer-by-id/99"))
                .willReturn(notFound()));

        given().contentType(ContentType.JSON)
                .body("{\"customerId\":99,\"movieId\":2,\"rentalCost\":75}")
                .when().post("/rental/create-rental")
                .then().statusCode(400);
    }
}
