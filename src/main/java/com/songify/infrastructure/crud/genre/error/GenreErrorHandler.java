package com.songify.infrastructure.crud.genre.error;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GenreErrorHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseBody
    @ResponseStatus
    public ResponseEntity<ErrorGenreResponseDto> handleGenreNotFoundException(GenreNotFoundException ex) {
        log.error("Error while trying to get Genre from db.");
        ErrorGenreResponseDto responseDto = new ErrorGenreResponseDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenreIsUsedBySongsException.class)
    @ResponseBody
    @ResponseStatus
    public ResponseEntity<ErrorGenreResponseDto> handleGenreNotFoundException(GenreIsUsedBySongsException ex) {
        log.error("Error while trying to delete Genre from db.");
        ErrorGenreResponseDto responseDto = new ErrorGenreResponseDto(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }
}
