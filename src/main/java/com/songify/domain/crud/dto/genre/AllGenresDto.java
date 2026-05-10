package com.songify.domain.crud.dto.genre;

import lombok.Builder;

import java.util.List;

@Builder
public record AllGenresDto(List<GenreDto> genres) {
}
