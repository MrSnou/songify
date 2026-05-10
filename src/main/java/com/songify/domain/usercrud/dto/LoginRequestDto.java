package com.songify.domain.usercrud.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

public record LoginRequestDto(
        @NonNull @NotEmpty
        String email,
        @NonNull @NotEmpty
        String password) {
}
