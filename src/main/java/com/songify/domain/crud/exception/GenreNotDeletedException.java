package com.songify.domain.crud.exception;

public class GenreNotDeletedException extends RuntimeException {

    public GenreNotDeletedException(final String message) {
        super(message);
    }
}
