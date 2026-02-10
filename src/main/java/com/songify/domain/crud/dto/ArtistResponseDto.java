package com.songify.domain.crud.dto;

import org.springframework.http.HttpStatus;

public record ArtistResponseDto(HttpStatus status, String message) {
}
