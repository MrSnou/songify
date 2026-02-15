package com.songify.infrastructure.crud.album.dto.response;

import org.springframework.http.HttpStatus;

public record AlbumResponseDto(HttpStatus status, String message, UpdateAlbumWithSongsAndArtistsResponseDto album) {
}
