package com.songify.song;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SongRequestDto(
        @NotNull(message = "songName cannot be null!")
        @NotEmpty(message = "songName cannot be empty!")
        String songName) {
}
