package se.yrgo.rental.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.rental.client.CustomerClient;
import se.yrgo.rental.data.RentalRepository;
import se.yrgo.rental.domain.Rental;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CustomerClient customerClient;
    @InjectMocks
    private RentalServiceImpl service;

    private Rental rentalFor(Long customerId) {
        Rental r = new Rental();
        r.setCustomerId(customerId);
        r.setMovieId(2L);
        r.setRentalCost(75);
        return r;
    }

    @Test
    void createRental_knownCustomer_saves() {
        when(customerClient.customerExists(1L)).thenReturn(true);
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> inv.getArgument(0));

        service.createRental(rentalFor(1L));

        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void createRental_unknownCustomer_throwsAndDoesNotSave() {
        when(customerClient.customerExists(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.createRental(rentalFor(99L)))
                .isInstanceOf(UnknownCustomerException.class);

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void findById_missing_throwsRentalNotFound() {
        when(rentalRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(5L))
                .isInstanceOf(RentalNotFoundException.class);
    }

    @Test
    void deleteRental_missing_throwsRentalNotFound() {
        when(rentalRepository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteRental(5L))
                .isInstanceOf(RentalNotFoundException.class);
        verify(rentalRepository, never()).deleteById(any());
    }

    @Test
    void findByCustomerId_delegatesToRepository() {
        when(rentalRepository.findByCustomerId(1L)).thenReturn(java.util.List.of(new Rental()));

        assertThat(service.findByCustomerId(1L)).hasSize(1);
    }
}
