package com.songify.domain.crud.dto.song;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SongRequestDto(
        @NotNull(message = "Name cannot be null")
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @Min(value = 1, message = "Duration must be a positive number")
        Long duration,
        Instant releaseDate,
        SongLanguageDto language
) {

}
