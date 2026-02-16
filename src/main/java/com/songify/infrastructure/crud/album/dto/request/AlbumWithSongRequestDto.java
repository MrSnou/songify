package com.songify.infrastructure.crud.album.dto.request;

import java.time.Instant;

public record AlbumWithSongRequestDto(String title, Instant releaseDate, Long songId) {
}
