package com.songify.infrastructure.crud.album.error;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(final String message) {
        super(message);
    }
}
