package se.yrgo.rental.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.yrgo.rental.domain.Rental;
import se.yrgo.rental.service.RentalNotFoundException;
import se.yrgo.rental.service.RentalService;
import se.yrgo.rental.service.UnknownCustomerException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalController.class)
class RentalControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @Test
    void findById_missing_maps404ViaAdvice() throws Exception {
        when(rentalService.findById(5L)).thenThrow(new RentalNotFoundException("nope"));

        mockMvc.perform(get("/rental/findById/5")).andExpect(status().isNotFound());
    }

    @Test
    void createRental_unknownCustomer_maps400ViaAdvice() throws Exception {
        when(rentalService.createRental(any(Rental.class)))
                .thenThrow(new UnknownCustomerException(99L));

        mockMvc.perform(post("/rental/create-rental")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":99,\"movieId\":2,\"rentalCost\":75}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRental_valid_returns201() throws Exception {
        when(rentalService.createRental(any(Rental.class))).thenReturn(new Rental());

        mockMvc.perform(post("/rental/create-rental")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"movieId\":2,\"rentalCost\":75}"))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteRental_isDeleteVerb_returns204() throws Exception {
        mockMvc.perform(delete("/rental/deleteRental/1")).andExpect(status().isNoContent());
        verify(rentalService).deleteRental(1L);
    }
}
