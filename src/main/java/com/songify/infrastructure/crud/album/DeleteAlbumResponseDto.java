package com.songify.infrastructure.crud.album;

import org.springframework.http.HttpStatus;

record DeleteAlbumResponseDto(HttpStatus status, String message) {
}
