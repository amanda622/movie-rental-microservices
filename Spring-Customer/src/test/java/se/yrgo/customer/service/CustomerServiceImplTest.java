package se.yrgo.customer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.customer.data.CustomerRepository;
import se.yrgo.customer.domain.Customer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void updateCustomer_existing_savesNewValues() {
        Customer existing = new Customer(1L, "Old", "old@mail.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Customer> result = service.updateCustomer(1L, new Customer(null, "New", "new@mail.com"));

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New");
        assertThat(result.get().getEmail()).isEqualTo("new@mail.com");
    }

    @Test
    void updateCustomer_missing_returnsEmpty() {
        when(customerRepository.findById(7L)).thenReturn(Optional.empty());

        assertThat(service.updateCustomer(7L, new Customer())).isEmpty();
        verify(customerRepository, never()).save(any());
    }
}
