package com.dm.rest.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@RequiredArgsConstructor
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiException extends RuntimeException{
    private final String message;
    private final HttpStatus httpStatus;

}
