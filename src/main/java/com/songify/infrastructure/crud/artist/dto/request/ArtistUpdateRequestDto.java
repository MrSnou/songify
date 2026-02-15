package com.songify.infrastructure.crud.artist.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtistUpdateRequestDto(
        @NotNull(message = "newArtistName cannot be null")
        @NotEmpty(message = "newArtistName cannot be empty")
        String newArtistName) {
}
