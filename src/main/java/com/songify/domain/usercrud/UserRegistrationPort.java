package com.songify.domain.usercrud;

public interface UserRegistrationPort {
    void register(String email, String password);
    boolean userExists(String email);
}
