package com.songify.infrastructure.crud.song.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PartiallyUpdateSongRequestDto(
        @NotNull(message = "name cannot be null")
        @NotEmpty(message = "name cannot be empty")
        String songName,

        @NotNull(message = "name cannot be null")
        @NotEmpty(message = "name cannot be empty")
        String artist
) {
}
