package com.songify.infrastructure.crud.genre.error;

public class GenreIsUsedBySongsException extends RuntimeException {
    public GenreIsUsedBySongsException(final String s) {
        super(s);
    }
}
