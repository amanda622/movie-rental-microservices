package se.yrgo.rental.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.rental.client.CustomerClient;
import se.yrgo.rental.data.RentalRepository;
import se.yrgo.rental.domain.Rental;
import se.yrgo.rental.domain.Review;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerClient customerClient;

    public RentalServiceImpl(RentalRepository rentalRepository, CustomerClient customerClient) {
        this.rentalRepository = rentalRepository;
        this.customerClient = customerClient;
    }

    @Override
    public Rental createRental(Rental rental) {
        requireKnownCustomer(rental.getCustomerId());
        return rentalRepository.save(rental);
    }

    @Override
    public Rental createRentalWithReview(Rental rental, Review review) {
        requireKnownCustomer(rental.getCustomerId());
        rental.setReview(review);
        return rentalRepository.save(rental);
    }

    /** The microservice boundary: validate the customer exists before persisting. */
    private void requireKnownCustomer(Long customerId) {
        if (customerId == null || !customerClient.customerExists(customerId)) {
            throw new UnknownCustomerException(customerId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findByDate(LocalDate date) {
        return rentalRepository.findByRentalDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public Rental findById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("No rental with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findByCustomerId(Long customerId) {
        return rentalRepository.findByCustomerId(customerId);
    }

    @Override
    public void deleteRental(Long id) {
        if (!rentalRepository.existsById(id)) {
            throw new RentalNotFoundException("No rental with id " + id);
        }
        rentalRepository.deleteById(id);
    }
}
