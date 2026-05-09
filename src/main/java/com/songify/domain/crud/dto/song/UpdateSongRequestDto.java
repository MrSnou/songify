package com.songify.domain.crud.dto.song;

import lombok.Builder;

@Builder
public record UpdateSongRequestDto(

        String songName,

        Long duration
) {
}
