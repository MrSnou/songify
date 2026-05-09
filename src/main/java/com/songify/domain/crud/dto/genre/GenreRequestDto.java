package com.songify.domain.crud.dto.genre;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GenreRequestDto(
        @NotEmpty(message = "Genre name cannot be empty")
        @NotNull(message = "Genre name cannot be null")
        String name) {
}
