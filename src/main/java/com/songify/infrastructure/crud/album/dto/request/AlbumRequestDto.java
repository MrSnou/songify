package com.songify.infrastructure.crud.album.dto.request;

import java.time.Instant;

public record AlbumRequestDto(String title, Instant releaseDate, Long songId) {
}
