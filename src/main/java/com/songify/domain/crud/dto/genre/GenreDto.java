package com.songify.domain.crud.dto.genre;

import lombok.Builder;

@Builder
public record GenreDto(Long id, String name) {
}
