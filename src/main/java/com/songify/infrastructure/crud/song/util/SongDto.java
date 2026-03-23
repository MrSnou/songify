package com.songify.infrastructure.crud.song.util;

import com.songify.infrastructure.crud.genre.GenreDto;
import lombok.Builder;

@Builder
public record SongDto(
        Long id,
        String name,
        Long duration,
        GenreDto genre) {
}
