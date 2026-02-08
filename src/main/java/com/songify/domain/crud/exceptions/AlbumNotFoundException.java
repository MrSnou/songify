package com.songify.domain.crud.exceptions;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(final String message) {
        super(message);
    }
}
