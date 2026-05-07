package com.songify.domain.crud.dto.song;

import com.songify.domain.crud.dto.genre.GenreDto;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SongDto(
        Long id,
        String name,
        Long duration,
        Instant releaseDate,
        GenreDto genre
        ) {
}
