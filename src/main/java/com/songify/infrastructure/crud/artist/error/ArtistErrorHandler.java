package com.songify.infrastructure.crud.artist.error;

import com.songify.domain.crud.exception.ArtistNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Log4j2
public class ArtistErrorHandler {

    @ExceptionHandler(ArtistNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ArtistErrorResponseDto> handleGenreNotFoundException(ArtistNotFoundException ex) {
        log.error("ArtistNotFoundException while accessing artist!");
        ArtistErrorResponseDto responseDto = new ArtistErrorResponseDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }
}
