package com.songify.infrastructure.crud.song.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record CreateSongRequestDto(
        @NotNull(message = "songName cannot be null!")
        @NotEmpty(message = "songName cannot be empty!")
        String songName,

        @NotNull(message = "songName cannot be null!")
        @NotEmpty(message = "songName cannot be empty!")
        String artist
) {


}
