package com.songify.infrastructure.crud.artist.dto.response;

import org.springframework.http.HttpStatus;

public record ArtistResponseDto(HttpStatus status, String message) {
}
