package com.songify.domain.crud.Exceptions;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(final String message) {
        super(message);
    }
}
