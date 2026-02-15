package com.songify.infrastructure.crud.song.dto.request;

public record UpdateSongRequestDto(

        String songName,

        Long duration
) {
}
