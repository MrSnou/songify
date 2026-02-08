package com.songify.infrastructure.crud.song.controller.error;


import com.songify.domain.crud.exceptions.AlbumNotEmptyException;
import com.songify.domain.crud.exceptions.AlbumNotFoundException;
import com.songify.infrastructure.crud.song.controller.error.dto.ErrorAlbumResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class AlbumErrorHandler {

    @ExceptionHandler(AlbumNotEmptyException.class)
    @ResponseBody
    public ResponseEntity<ErrorAlbumResponseDto> handleAlbumNotEmptyException(final AlbumNotEmptyException ex) {
        log.warn(ex.getMessage());
        ErrorAlbumResponseDto responseDto = new ErrorAlbumResponseDto(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlbumNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorAlbumResponseDto> handleAlbumNotFoundException(final AlbumNotFoundException ex) {
        log.warn(ex.getMessage());
        ErrorAlbumResponseDto responseDto = new ErrorAlbumResponseDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }


}
