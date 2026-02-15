package com.songify.infrastructure.crud.song.dto.request;


import com.songify.infrastructure.crud.song.util.SongLanguageDto;

import java.time.Instant;

public record SongRequestDto(
        String name,
        Long duration,
        Instant releaseDate,
        SongLanguageDto language
) {

}
