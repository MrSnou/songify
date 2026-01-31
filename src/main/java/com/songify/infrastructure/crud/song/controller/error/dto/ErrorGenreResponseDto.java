package com.songify.infrastructure.crud.song.controller.error.dto;

import org.springframework.http.HttpStatus;

public record ErrorGenreResponseDto(HttpStatus status, String message) {
}
