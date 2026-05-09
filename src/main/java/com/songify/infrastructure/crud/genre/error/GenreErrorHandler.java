package com.songify.infrastructure.crud.genre.error;


import com.songify.domain.crud.exception.GenreDefaultIsLockedException;
import com.songify.domain.crud.exception.GenreIsUsedBySongsException;
import com.songify.domain.crud.exception.GenreNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class GenreErrorHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorGenreResponseDto> handleGenreNotFoundException(GenreNotFoundException ex) {
        log.error("GenreNotFoundException while accessing genre! " + "[" + LocalDateTime.now() + "]");
        ErrorGenreResponseDto responseDto = new ErrorGenreResponseDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenreIsUsedBySongsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorGenreResponseDto> handleGenreNotFoundException(GenreIsUsedBySongsException ex) {
        log.error("GenreIsUsedBySongsException while accessing genre! " + "[" + LocalDateTime.now() + "]");
        ErrorGenreResponseDto responseDto = new ErrorGenreResponseDto(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GenreDefaultIsLockedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorGenreResponseDto> handleGenreDefaultIsLockedException(GenreDefaultIsLockedException ex) {
        log.error("GenreDefaultIsLockerException while accessing genre! " + "[" + LocalDateTime.now() + "]");
        ErrorGenreResponseDto responseDto = new ErrorGenreResponseDto(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }
}
