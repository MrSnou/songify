package com.songify.infrastructure.crud.genre.dto;

import lombok.Builder;

@Builder
public record GenreRequestDto(String name) {
}
