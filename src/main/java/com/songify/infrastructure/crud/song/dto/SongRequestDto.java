package com.songify.infrastructure.crud.song.dto;


import com.songify.domain.crud.dto.song.SongLanguageDto;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SongRequestDto(
        String name,
        Long duration,
        Instant releaseDate,
        SongLanguageDto language
) {

}
