package com.songify.domain.crud.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtistUpdateRequestDto(
        @NotNull(message = "newArtistName cannot be null")
        @NotEmpty(message = "newArtistName cannot be empty")
        String newArtistName) {
}
