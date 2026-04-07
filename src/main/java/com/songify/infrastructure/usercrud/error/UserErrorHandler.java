package com.songify.infrastructure.usercrud.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

class UserErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<UserNotFoundResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        UserNotFoundResponseDto errorSongResponseDto = new UserNotFoundResponseDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorSongResponseDto);

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<UserAlreadyExistsResponseDto> handleUserNotFoundException(UserAlreadyExistsException ex) {
        UserAlreadyExistsResponseDto errorSongResponseDto = new UserAlreadyExistsResponseDto(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorSongResponseDto);

    }
}
