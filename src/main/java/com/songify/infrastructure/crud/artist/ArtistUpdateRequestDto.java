package com.songify.infrastructure.crud.artist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

record ArtistUpdateRequestDto(
        @NotNull(message = "newArtistName cannot be null")
        @NotEmpty(message = "newArtistName cannot be empty")
        String newArtistName) {
}
