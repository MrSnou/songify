package com.songify.infrastructure.usercrud.dto;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(
        String message
) {
}
