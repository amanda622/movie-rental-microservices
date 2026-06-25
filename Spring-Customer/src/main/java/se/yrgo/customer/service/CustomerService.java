package se.yrgo.customer.service;

import se.yrgo.customer.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    Customer createCustomer(Customer customer);

    Optional<Customer> updateCustomer(Long customerId, Customer updatedCustomer);

    void deleteCustomer(Long customerId);
}
