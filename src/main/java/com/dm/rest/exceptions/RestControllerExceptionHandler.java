package com.dm.rest.exceptions;

import com.dm.rest.payload.response.ApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApplicationResponse> catchResourceNotFoundException(UserNotFoundException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApplicationResponse> catchApplicationException(ApiException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidateTokenException.class)
    public ResponseEntity<ApplicationResponse> catchResourceNotFoundException(ValidateTokenException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApplicationResponse> catchAccessDeniedException(AccessDeniedException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApplicationResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
