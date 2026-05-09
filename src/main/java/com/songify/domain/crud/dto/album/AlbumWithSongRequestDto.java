package com.songify.domain.crud.dto.album;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record AlbumWithSongRequestDto(
        @NotEmpty(message = "Title cannot be empty.")
        @NotNull(message = "Title cannot be null.")
        String title,
        Instant releaseDate,
        @NotNull(message = "Add at least 1 song id.")
        @NotEmpty(message = "Add at least 1 song id.")
        List<Long> songIds) {
}
