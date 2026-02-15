package com.songify.infrastructure.crud.genre.error;

public class GenreNotDeletedException extends RuntimeException {

    public GenreNotDeletedException(final String message) {
        super(message);
    }
}
