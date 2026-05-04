package com.songify.infrastructure.usercrud.controller;

import com.songify.domain.usercrud.UserLoginService;
import com.songify.infrastructure.security.jwt.TokenCookieService;
import com.songify.infrastructure.usercrud.dto.LoginRequestDto;
import com.songify.infrastructure.usercrud.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class LoginController {

    private final UserLoginService userLoginService;
    private final TokenCookieService tokenCookieService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request, HttpServletResponse response) {
        String token = userLoginService.login(request);
        tokenCookieService.addTokenCookieToResponse(response, token);
        return ResponseEntity.ok(new LoginResponseDto("Login successful!"));
    }
}
