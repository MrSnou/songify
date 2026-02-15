package com.songify.infrastructure.crud.song.error;

import org.springframework.http.HttpStatus;

record ErrorSongResponseDto(HttpStatus status, String message) {
}
