package com.songify.infrastructure.security.jwt;

import com.songify.infrastructure.security.jwt.dto.TokenRequestDto;
import com.songify.infrastructure.security.jwt.dto.TokenResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
class JwtController {

    private final JwtTokenGenerator tokenGenerator;

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> f(@RequestBody TokenRequestDto requestDto) {
        String generatedToken = tokenGenerator
                .authenticateAndGenerateToken(requestDto.username(), requestDto.password());

        return ResponseEntity.ok(TokenResponseDto.builder()
                        .token(generatedToken)
                .build());

    }
}
