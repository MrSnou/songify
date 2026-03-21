package com.songify.infrastructure.crud.song.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record DeleteSongResponseDto(String message, HttpStatus status) {
}
