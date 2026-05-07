package com.songify.domain.usercrud.dto;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(
        String message
) {
}
