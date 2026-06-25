package se.yrgo.customer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Showcase integration test running against a REAL PostgreSQL in Docker via
 * Testcontainers — proving the app is database-agnostic and exercising the full
 * stack (HTTP -> Spring -> JPA -> Postgres). Named *IT so Failsafe runs it in `verify`.
 * <p>
 * {@code @EnabledIf} skips the whole class (before any container starts) when no
 * Testcontainers-compatible Docker environment is present, so a local
 * {@code mvn verify} stays green on machines without Docker; CI runs it for real.
 */
@EnabledIf("dockerAvailable")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerPostgresIT {

    static boolean dockerAvailable() {
        return DockerClientFactory.instance().isDockerAvailable();
    }

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createAndFetchCustomer_persistsToPostgres() {
        int id = given().contentType(ContentType.JSON)
                .body("{\"name\":\"Grace\",\"email\":\"grace@mail.com\"}")
                .when().post("/customer/create-customer")
                .then().statusCode(201)
                .extract().path("id");

        given().when().get("/customer/get-customer-by-id/" + id)
                .then().statusCode(200)
                .body("name", equalTo("Grace"));
    }
}
