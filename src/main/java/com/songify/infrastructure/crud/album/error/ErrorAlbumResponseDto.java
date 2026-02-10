package com.songify.infrastructure.crud.album.error;

import org.springframework.http.HttpStatus;

record ErrorAlbumResponseDto(HttpStatus status, String message) {
}
