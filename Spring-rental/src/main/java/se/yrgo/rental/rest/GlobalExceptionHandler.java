package se.yrgo.rental.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.yrgo.rental.service.RentalNotFoundException;
import se.yrgo.rental.service.UnknownCustomerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RentalNotFoundException.class)
    public ProblemDetail handleNotFound(RentalNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UnknownCustomerException.class)
    public ProblemDetail handleUnknownCustomer(UnknownCustomerException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
