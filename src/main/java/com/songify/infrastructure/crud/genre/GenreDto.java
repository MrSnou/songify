package com.songify.infrastructure.crud.genre;

import lombok.Builder;

@Builder
public record GenreDto(Long id, String name) {
}
