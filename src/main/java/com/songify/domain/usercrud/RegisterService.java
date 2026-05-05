package com.songify.domain.usercrud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserRegistrationPort userRegistrationPort;

    public String registerUser(final String email, final String password) {
        userRegistrationPort.register(email, password);
        return email;
    }
}
