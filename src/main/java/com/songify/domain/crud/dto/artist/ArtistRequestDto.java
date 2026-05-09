package com.songify.domain.crud.dto.artist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ArtistRequestDto(
        @NotNull(message = "Name cannot be null")
        @NotEmpty(message = "Name cannot be empty")
        String name) {
}
