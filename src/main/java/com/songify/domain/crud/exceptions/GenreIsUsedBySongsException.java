package com.songify.domain.crud.exceptions;

public class GenreIsUsedBySongsException extends RuntimeException {
    public GenreIsUsedBySongsException(final String s) {
        super(s);
    }
}
