package com.vrto;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
class TicketController {

    @RequestMapping(value = "/{companyId}/{userId}/tickets", method = GET)
    public List<Ticket> listAllForCompanyUser(@PathVariable long companyId, @PathVariable long userId) {
        return Arrays.asList(
                new Ticket(1L, "First ticket"),
                new Ticket(2L, "Second ticket"),
                new Ticket(3L, "Third ticket"));
    }

    @ExceptionHandler(AccessViolationException.class)
    ResponseEntity<ErrorMessage> accessViolationHandler(Exception exception) {
        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @Value
    private static class ErrorMessage {
        String message;
    }

}
