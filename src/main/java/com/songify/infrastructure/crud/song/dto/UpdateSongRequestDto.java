package com.songify.infrastructure.crud.song.dto;

import lombok.Builder;

@Builder
public record UpdateSongRequestDto(

        String songName,

        Long duration
) {
}
