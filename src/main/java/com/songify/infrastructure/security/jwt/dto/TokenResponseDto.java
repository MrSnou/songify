package com.songify.infrastructure.security.jwt.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(String token) {
}
