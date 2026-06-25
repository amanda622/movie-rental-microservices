package se.yrgo.rental.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.yrgo.rental.domain.Rental;
import se.yrgo.rental.domain.Review;
import se.yrgo.rental.service.RentalService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rental")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/getAllRentals")
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/findById/{id}")
    public Rental findById(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @GetMapping("/findByCustomerId/{id}")
    public List<Rental> findByCustomerId(@PathVariable Long id) {
        return rentalService.findByCustomerId(id);
    }

    @GetMapping("/findByDate/{date}")
    public List<Rental> findByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return rentalService.findByDate(date);
    }

    @PostMapping("/create-rental")
    @ResponseStatus(HttpStatus.CREATED)
    public Rental createRental(@RequestBody Rental rental) {
        return rentalService.createRental(rental);
    }

    @PostMapping("/createRentalWithReview")
    @ResponseStatus(HttpStatus.CREATED)
    public Rental createRentalWithReview(@RequestBody RentalWithReviewRequest request) {
        return rentalService.createRentalWithReview(request.rental(), request.review());
    }

    // legacy bug: deletion was a GET that silently no-op'd; now a real DELETE returning 204/404.
    @DeleteMapping("/deleteRental/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
    }

    public record RentalWithReviewRequest(Rental rental, Review review) {
    }
}
