package com.example.springtest.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "CPF cannot be used")
public class CpfExistsException extends RuntimeException {

  private String errorMessage = "CPF is invalid";
}