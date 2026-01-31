package com.songify.domain.crud.Exceptions;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(final String message) {
        super(message);
    }
}
