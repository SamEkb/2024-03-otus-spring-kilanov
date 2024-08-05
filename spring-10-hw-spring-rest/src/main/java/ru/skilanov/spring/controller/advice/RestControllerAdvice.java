package ru.skilanov.spring.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skilanov.spring.dto.ErrorResponse;
import ru.skilanov.spring.exception.NotFoundException;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
