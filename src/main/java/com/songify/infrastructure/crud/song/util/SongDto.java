package com.songify.infrastructure.crud.song.util;

import lombok.Builder;

@Builder
public record SongDto(
        Long id,
        String name,
        Long duration) {
}
