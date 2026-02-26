package com.songify.infrastructure.crud.genre.dto.request;

import lombok.Builder;

@Builder
public record GenreRequestDto(String name) {
}
