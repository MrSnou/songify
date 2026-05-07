package com.songify.domain.crud.dto.song;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record DeleteSongResponseDto(String message) {
}
