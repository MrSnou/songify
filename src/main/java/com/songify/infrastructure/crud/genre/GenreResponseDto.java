package com.songify.infrastructure.crud.genre;

import org.springframework.http.HttpStatus;

record GenreResponseDto(HttpStatus status, String message) {
}
