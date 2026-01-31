package com.songify.infrastructure.crud.artist;

import org.springframework.http.HttpStatus;

record ArtistResponseDto(HttpStatus status, String message) {
}
