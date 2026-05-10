package com.songify.infrastructure.usercrud.controller;

import com.songify.domain.usercrud.RegisterService;
import com.songify.domain.usercrud.dto.UserRegisterRequestDto;
import com.songify.domain.usercrud.dto.UserRegisterResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto request) {
        UserRegisterResponseDto userRegisterResponseDto = registerService.registerUser(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRegisterResponseDto);
    }
}
