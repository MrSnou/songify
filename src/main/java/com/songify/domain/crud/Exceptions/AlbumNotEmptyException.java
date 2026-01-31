package com.songify.domain.crud.Exceptions;

public class AlbumNotEmptyException extends RuntimeException {
    public AlbumNotEmptyException(final String message) {
        super(message);
    }
}
