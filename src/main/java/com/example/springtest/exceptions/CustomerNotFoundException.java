package com.example.springtest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "This Customer has not been found")
public class CustomerNotFoundException extends RuntimeException {

}