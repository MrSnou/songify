package com.songify.domain.crud.exception;

public class GenreIsUsedBySongsException extends RuntimeException {
    public GenreIsUsedBySongsException(final String s) {
        super(s);
    }
}
