package com.songify.infrastructure.crud.song.controller.error;

import org.springframework.http.HttpStatus;

record ErrorSongResponseDto(HttpStatus status, String message) {
}
