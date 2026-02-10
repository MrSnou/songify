package com.songify.infrastructure.crud.genre.error;

import org.springframework.http.HttpStatus;

record ErrorGenreResponseDto(HttpStatus status, String message) {
}
