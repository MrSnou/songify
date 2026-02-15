package com.songify.infrastructure.crud.song.util;

import com.songify.infrastructure.crud.genre.GenreDto;

import java.time.Instant;

public record SongInfoDto(
        Long id,
        String name,
        Long duration,
        Instant releaseDate,
        GenreDto genreDto
        ) {
}
