package com.dm.rest.exceptions;

import com.dm.rest.payload.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> catchResourceNotFoundException(UserNotFoundException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> catchApplicationException(ApiException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidateTokenException.class)
    public ResponseEntity<ApiResponse> catchResourceNotFoundException(ValidateTokenException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> catchAccessDeniedException(AccessDeniedException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

}
