package com.songify.infrastructure.crud.genre.dto.response;

import org.springframework.http.HttpStatus;

public record GenreResponseDto(HttpStatus status, String message) {
}
