package com.songify.infrastructure.usercrud.controller;

import com.songify.infrastructure.usercrud.dto.UserRegisterRequestDto;
import com.songify.infrastructure.usercrud.dto.UserRegisterResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
class RegisterController {

    private final UserDetailsManager userDetailsManager;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@RequestBody UserRegisterRequestDto request) {
        String email = request.email();
        String password = request.password();
        UserDetails user = User.builder()
                .username(email)
                .password(password)
                .build();
        userDetailsManager.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserRegisterResponseDto("User with email " + email + " successfully registered."));
    }
}
