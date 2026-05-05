package com.songify.domain.usercrud.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(final String s) {
        super(s);
    }
}
