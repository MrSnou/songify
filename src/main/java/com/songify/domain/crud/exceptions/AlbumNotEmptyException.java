package com.songify.domain.crud.exceptions;

public class AlbumNotEmptyException extends RuntimeException {
    public AlbumNotEmptyException(final String message) {
        super(message);
    }
}
