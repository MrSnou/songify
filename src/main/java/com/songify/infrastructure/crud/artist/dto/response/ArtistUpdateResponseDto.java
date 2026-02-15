package com.songify.infrastructure.crud.artist.dto.response;

import org.springframework.http.HttpStatus;

public record ArtistUpdateResponseDto(HttpStatus status, String message) {
}
