package se.yrgo.rental.service;

/**
 * Thrown when a rental references a customer that customer-service does not know.
 * Maps to HTTP 400 (the client sent a bad reference).
 */
public class UnknownCustomerException extends RuntimeException {
    public UnknownCustomerException(Long customerId) {
        super("Unknown customer id: " + customerId);
    }
}
