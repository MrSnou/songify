package com.songify.domain.crud.Exceptions;

public class GenreIsUsedBySongsException extends RuntimeException {
    public GenreIsUsedBySongsException(final String s) {
        super(s);
    }
}
