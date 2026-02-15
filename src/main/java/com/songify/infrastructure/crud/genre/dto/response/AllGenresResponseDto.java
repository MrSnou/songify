package com.songify.infrastructure.crud.genre.dto.response;

import com.songify.infrastructure.crud.genre.GenreDto;

import java.util.List;

public record AllGenresResponseDto(String message, List<GenreDto> genreDto) {
}
