package com.songify.infrastructure.crud.song.dto.request;

import lombok.Builder;

@Builder
public record UpdateSongRequestDto(

        String songName,

        Long duration
) {
}
