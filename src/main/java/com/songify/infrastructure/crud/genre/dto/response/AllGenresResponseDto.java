package com.songify.infrastructure.crud.genre.dto.response;

import com.songify.domain.crud.dto.GenreDto;

import java.util.List;

public record AllGenresResponseDto(String message, List<GenreDto> genreDto) {
}
