package se.yrgo.rental.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.yrgo.rental.domain.Rental;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByRentalDate(LocalDate rentalDate);

    List<Rental> findByCustomerId(Long customerId);
}
