package com.songify.infrastructure.crud.genre.error;

public class GenreDefaultIsLockedException extends RuntimeException {
    public GenreDefaultIsLockedException(final String message) {
        super(message);
    }
}
