package com.songify.infrastructure.usercrud.error;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(final String s) {
        super(s);
    }
}
