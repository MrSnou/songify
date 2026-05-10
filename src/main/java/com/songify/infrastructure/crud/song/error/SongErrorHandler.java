package com.songify.infrastructure.crud.song.error;

import com.songify.domain.crud.exception.SongNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class SongErrorHandler {

    @ExceptionHandler(SongNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorSongResponseDto> handleSongNotFoundException(SongNotFoundException e) {
        log.warn("SongNotFoundException while accessing song!");
        ErrorSongResponseDto errorSongResponseDto = new ErrorSongResponseDto(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorSongResponseDto);
    }
}
