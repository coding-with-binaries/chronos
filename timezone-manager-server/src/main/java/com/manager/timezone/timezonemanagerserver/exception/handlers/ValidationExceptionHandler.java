package com.manager.timezone.timezonemanagerserver.exception.handlers;

import com.manager.timezone.timezonemanagerserver.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class ValidationExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleException(MethodArgumentNotValidException exception) {
        String errorMessage;
        try {
            errorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } catch (Exception e) {
            errorMessage = "The request inputs are not valid";
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
