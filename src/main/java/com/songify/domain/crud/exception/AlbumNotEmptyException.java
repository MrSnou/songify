package com.songify.domain.crud.exception;

public class AlbumNotEmptyException extends RuntimeException {
    public AlbumNotEmptyException(final String message) {
        super(message);
    }
}
