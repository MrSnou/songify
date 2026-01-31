package com.songify.domain.crud.Exceptions;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(final String message) {
        super(message);
    }
}
