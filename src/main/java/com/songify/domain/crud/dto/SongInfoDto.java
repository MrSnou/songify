package com.songify.domain.crud.dto;

import java.time.Instant;

public record SongInfoDto(
        Long id,
        String name,
        Long duration,
        Instant releaseDate,
        GenreDto genreDto
        ) {
}
