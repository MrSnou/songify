package com.songify.domain.crud.dto;

import org.springframework.http.HttpStatus;

public record ArtistUpdateResponseDto(HttpStatus status, String message) {
}
