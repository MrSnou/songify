package com.songify.infrastructure.crud.artist;

import org.springframework.http.HttpStatus;

record ArtistUpdateResponseDto(HttpStatus status, String message) {
}
