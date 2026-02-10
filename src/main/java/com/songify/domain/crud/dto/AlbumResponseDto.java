package com.songify.domain.crud.dto;

import org.springframework.http.HttpStatus;

public record AlbumResponseDto(HttpStatus status, String message, UpdateAlbumWithSongsAndArtistsResponseDto album) {
}
