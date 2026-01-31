package com.songify.infrastructure.crud.song.controller.error.dto;

import org.springframework.http.HttpStatus;

public record ArtistErrorResponseDto(HttpStatus status, String message) {
}
