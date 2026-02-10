package com.songify.domain.crud.dto;

import org.springframework.http.HttpStatus;

public record DeleteAlbumResponseDto(HttpStatus status, String message) {
}
