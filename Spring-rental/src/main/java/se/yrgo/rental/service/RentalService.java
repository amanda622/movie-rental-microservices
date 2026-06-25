package se.yrgo.rental.service;

import se.yrgo.rental.domain.Rental;
import se.yrgo.rental.domain.Review;

import java.time.LocalDate;
import java.util.List;

public interface RentalService {

    Rental createRental(Rental rental);

    Rental createRentalWithReview(Rental rental, Review review);

    List<Rental> getAllRentals();

    List<Rental> findByDate(LocalDate date);

    Rental findById(Long id);

    List<Rental> findByCustomerId(Long customerId);

    void deleteRental(Long id);
}
