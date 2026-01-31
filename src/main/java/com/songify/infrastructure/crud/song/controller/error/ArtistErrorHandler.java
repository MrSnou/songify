package com.songify.infrastructure.crud.song.controller.error;

import com.songify.domain.crud.Exceptions.ArtistNotFoundException;
import com.songify.domain.crud.Exceptions.GenreNotFoundException;
import com.songify.infrastructure.crud.song.controller.error.dto.ArtistErrorResponseDto;
import com.songify.infrastructure.crud.song.controller.error.dto.ErrorGenreResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class ArtistErrorHandler {

    @ExceptionHandler(ArtistNotFoundException.class)
    @ResponseBody
    @ResponseStatus
    public ResponseEntity<ArtistErrorResponseDto> handleGenreNotFoundException(ArtistNotFoundException ex) {
        log.error("Error while trying to get Genre from db.");
        ArtistErrorResponseDto responseDto = new ArtistErrorResponseDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }
}
