package com.songify.infrastructure.crud.album.dto.request;

import lombok.Builder;

import java.time.Instant;
@Builder
public record AlbumWithSongRequestDto(String title, Instant releaseDate, Long songId) {
}
