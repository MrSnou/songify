package com.songify.infrastructure.crud.album.error;

public class AlbumNotEmptyException extends RuntimeException {
    public AlbumNotEmptyException(final String message) {
        super(message);
    }
}
