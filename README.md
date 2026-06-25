# Movie-Rental Microservices

A small movie-rental system built as **three Spring Boot microservices** plus a Vue
frontend, used as a portfolio piece for **Java developer / QA test-automation** roles.
The emphasis is a real, layered **automated-testing strategy** and a one-command Docker
deployment.

```
                         ┌─────────────────────┐
        browser  ───────▶│  frontend (Vue/nginx)│  :8088
                         └─────────┬───────────┘
            ┌──────────────────────┼───────────────────────┐
            ▼                      ▼                        ▼
   ┌────────────────┐    ┌────────────────┐       ┌────────────────┐
   │ customer-service│   │ movies-service │       │ rental-service │
   │      :8080      │   │     :8081      │       │     :8082      │
   │   H2 (own DB)   │   │  H2 (own DB)   │       │  H2 (own DB)   │
   └────────▲────────┘   └────────────────┘       └───────┬────────┘
            │                                             │
            └───────── GET /customer/{id} ◀───────────────┘
                       (rental validates the customer exists)
```

## Services

| Service | Port | Responsibility |
|---|---|---|
| `customer-service` (`Spring-Customer`) | 8080 | CRUD for customers |
| `movies-service` (`Spring-movies`) | 8081 | Movie catalogue + genres + series |
| `rental-service` (`Spring-rental`) | 8082 | Rentals & reviews; **calls customer-service** to validate the customer |
| `frontend` (`Spring-frontendVue`) | 8088 | Vue UI |

Stack: **Java 17 · Spring Boot 3.2 · Spring Data JPA · H2 · Maven (multi-module reactor)**.

## The test pyramid

Run everything with one command (`./mvnw verify`). Each layer answers a different question:

| Layer | Tool | What it proves | Example |
|---|---|---|---|
| **Unit** | JUnit 5 + Mockito | Business logic in isolation | `RentalServiceImplTest` |
| **Web slice** | `@WebMvcTest` + MockMvc | HTTP status codes, JSON, routing, validation | `CustomerControllerWebMvcTest` |
| **Persistence slice** | `@DataJpaTest` + H2 | Custom queries against a real DB | `MoviesRepositoryDataJpaTest` |
| **Integration** | `@SpringBootTest` + REST Assured | The whole app over HTTP + DB | `MovieApiIT` |
| **Contract** | WireMock | The `CustomerClient` against a stubbed customer-service | `CustomerClientWireMockTest` |
| **Testcontainers** | Testcontainers + Postgres | DB-agnostic, real Postgres in Docker | `CustomerPostgresIT` |
| **End-to-end** | docker compose + REST Assured | All three services talking to each other | `MovieRentalFlowIT` |

~34 tests total. The Testcontainers test auto-skips when no Docker is available locally
and runs for real in CI; the E2E module only runs under the `e2e` profile against a live
stack.

## Run the tests

```bash
./mvnw verify                       # unit + slice + integration (+ Testcontainers if Docker present)
./mvnw -pl Spring-rental verify     # a single service
```

## Deploy locally (Docker Compose)

```bash
docker compose up --build           # builds 4 images and starts the stack
# UI:        http://localhost:8088
# movies:    http://localhost:8081/movies/get-all-movies
# rentals:   http://localhost:8082/rental/getAllRentals
docker compose down
```

If host port 8080 is taken, override just the customer host port:

```bash
CUSTOMER_HOST_PORT=18080 docker compose up --build
```

## Run the end-to-end tests against the running stack

```bash
docker compose up -d --build
./mvnw -Pe2e -pl e2e-tests verify
docker compose down
```

## CI

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs on every push/PR:

1. **test** – `./mvnw verify` (the full pyramid, incl. Testcontainers).
2. **e2e** – boots the stack with docker compose and runs the cross-service tests.

## What changed from the original school project

See [`Docs/SpringProjekt/springprojekt-upgrade-explained.md`](../../Docs/SpringProjekt/springprojekt-upgrade-explained.md)
for the full story (bugs fixed, why each test layer exists, the microservices-testing approach).
