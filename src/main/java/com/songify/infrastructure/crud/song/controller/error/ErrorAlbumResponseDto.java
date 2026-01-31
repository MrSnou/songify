package com.songify.infrastructure.crud.song.controller.error;

import org.springframework.http.HttpStatus;

record ErrorAlbumResponseDto(HttpStatus status, String message) {
}
