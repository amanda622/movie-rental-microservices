package se.yrgo.rental.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Consumer-side contract test: drives the real CustomerClient against a WireMock
 * stub of customer-service. Verifies how our client behaves for the 200 and 404
 * responses we depend on — no other service needs to be running.
 */
class CustomerClientWireMockTest {

    @RegisterExtension
    static WireMockExtension customerService = WireMockExtension.newInstance()
            .options(options().dynamicPort())
            .build();

    private CustomerClient client() {
        return new CustomerClient(customerService.baseUrl());
    }

    @Test
    void customerExists_returnsTrue_on200() {
        customerService.stubFor(get(urlEqualTo("/customer/get-customer-by-id/1"))
                .willReturn(okJson("{\"id\":1,\"name\":\"Ada\",\"email\":\"ada@mail.com\"}")));

        assertThat(client().customerExists(1L)).isTrue();
    }

    @Test
    void customerExists_returnsFalse_on404() {
        customerService.stubFor(get(urlEqualTo("/customer/get-customer-by-id/99"))
                .willReturn(notFound()));

        assertThat(client().customerExists(99L)).isFalse();
    }
}
