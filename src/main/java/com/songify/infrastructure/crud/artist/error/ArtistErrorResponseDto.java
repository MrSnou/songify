package com.songify.infrastructure.crud.artist.error;

import org.springframework.http.HttpStatus;

record ArtistErrorResponseDto(HttpStatus status, String message) {
}
