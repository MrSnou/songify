package com.songify.domain.usercrud;

import com.songify.domain.usercrud.dto.UserRegisterResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserRegistrationPort userRegistrationPort;

    public UserRegisterResponseDto registerUser(final String email, final String password) {
        userRegistrationPort.register(email, password);
        return UserRegisterResponseDto.builder()
                .message("User " + email + " registered successfully!")
                .build();
    }
}
