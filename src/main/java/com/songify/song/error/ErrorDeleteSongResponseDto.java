package com.songify.song.dto;

import org.springframework.http.HttpStatus;

public record ErrorDeleteSongResponseDto(String message, HttpStatus status) {
}
