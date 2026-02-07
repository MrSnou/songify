package com.songify.infrastructure.apivalidation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
class ApiValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> messages = getErrorsFromException(ex);
        ApiValidationErrorResponseDto response = new ApiValidationErrorResponseDto(HttpStatus.BAD_REQUEST, messages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private List<String> getErrorsFromException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }
}

