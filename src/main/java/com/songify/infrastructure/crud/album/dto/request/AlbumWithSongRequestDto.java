package com.songify.infrastructure.crud.album.dto.request;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record AlbumWithSongRequestDto(String title, Instant releaseDate, List<Long> songIds) {
}
