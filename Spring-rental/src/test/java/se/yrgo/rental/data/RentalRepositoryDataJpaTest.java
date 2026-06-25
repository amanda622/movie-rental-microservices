package se.yrgo.rental.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.yrgo.rental.domain.Rental;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RentalRepositoryDataJpaTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    void findByCustomerId_returnsOnlyThatCustomersRentals() {
        rentalRepository.save(rental(1L, LocalDate.of(2024, 1, 1)));
        rentalRepository.save(rental(1L, LocalDate.of(2024, 2, 1)));
        rentalRepository.save(rental(2L, LocalDate.of(2024, 3, 1)));

        assertThat(rentalRepository.findByCustomerId(1L)).hasSize(2);
    }

    @Test
    void findByRentalDate_matchesExactDate() {
        rentalRepository.save(rental(1L, LocalDate.of(2024, 1, 1)));
        rentalRepository.save(rental(2L, LocalDate.of(2024, 1, 1)));
        rentalRepository.save(rental(3L, LocalDate.of(2024, 6, 6)));

        assertThat(rentalRepository.findByRentalDate(LocalDate.of(2024, 1, 1))).hasSize(2);
    }

    private Rental rental(Long customerId, LocalDate date) {
        Rental r = new Rental();
        r.setCustomerId(customerId);
        r.setMovieId(10L);
        r.setRentalCost(50);
        r.setRentalDate(date);
        return r;
    }
}
