package com.songify.domain.crud.dto;


import java.time.Instant;

public record SongRequestDto(
        String name,
        Long duration,
        Instant releaseDate,
        SongLanguageDto language
) {

}
