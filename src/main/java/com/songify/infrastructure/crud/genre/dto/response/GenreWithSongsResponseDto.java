package com.songify.infrastructure.crud.genre.dto.response;

import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.SongDto;

import java.util.List;

public record GenreWithSongsResponseDto(String message, GenreDto genreDto, List<SongDto> songs) {
}
