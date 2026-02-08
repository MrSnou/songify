package com.songify.domain.crud.exceptions;

public class GenreNotDeletedException extends RuntimeException {

    public GenreNotDeletedException(final String message) {
        super(message);
    }
}
