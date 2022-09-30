package com.innrate.common.config.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.innrate.common.dto.ErrorDto;
import com.innrate.common.exceptions.ApiException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;

@ControllerAdvice
public class ExceptionAdviceController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ErrorDto> handleOfferConverterException(ApiException ex) {
        return prepareResponse(ex, ex.getStatus());
    }

    @ExceptionHandler({ServletException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErrorDto> handleServletException(ServletException ex) {
        return prepareResponse(ex, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return prepareResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        return prepareResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDto> handleAllException(Throwable ex) {
        return prepareResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> prepareResponse(Throwable e, HttpStatus status) {
        log.error("Status: {}, exception: {}", status, e);

        ErrorDto errorDto = new ErrorDto(e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(errorDto, headers, status);
    }
}

