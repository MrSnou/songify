package com.songify.domain.crud.dto.genre;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record GenreResponseDto(String message) {
}
