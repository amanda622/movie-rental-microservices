package se.yrgo.customer.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.yrgo.customer.domain.Customer;
import se.yrgo.customer.service.CustomerService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void getCustomerById_missing_returns404() throws Exception {
        when(customerService.getCustomerById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customer/get-customer-by-id/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_valid_returns201() throws Exception {
        when(customerService.createCustomer(any(Customer.class)))
                .thenReturn(new Customer(1L, "Ada", "ada@mail.com"));

        mockMvc.perform(post("/customer/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ada\",\"email\":\"ada@mail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createCustomer_invalidEmail_returns400() throws Exception {
        // Bean validation (@Email/@NotBlank) should reject this before the service is called.
        mockMvc.perform(post("/customer/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ada\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }
}
