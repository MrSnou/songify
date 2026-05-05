package com.songify.domain.crud.exception;

public class GenreDefaultIsLockedException extends RuntimeException {
    public GenreDefaultIsLockedException(final String message) {
        super(message);
    }
}
