package se.yrgo.rental.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Synchronous client for the customer-service. This is the real microservice
 * boundary: before a rental is created we ask the customer-service whether the
 * referenced customer actually exists. Base URL is configurable
 * ({@code customer.service.url}) so it can point at localhost in dev, the
 * compose service name in Docker, or a WireMock stub in tests.
 */
@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(@Value("${customer.service.url}") String customerServiceUrl) {
        this.restClient = RestClient.create(customerServiceUrl);
    }

    /**
     * @return true if customer-service returns 2xx for the id, false on 404.
     */
    public boolean customerExists(Long customerId) {
        return Boolean.TRUE.equals(restClient.get()
                .uri("/customer/get-customer-by-id/{id}", customerId)
                .exchange((request, response) -> response.getStatusCode().is2xxSuccessful()));
    }
}
