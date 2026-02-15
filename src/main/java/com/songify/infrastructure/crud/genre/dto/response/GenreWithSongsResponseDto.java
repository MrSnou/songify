package com.songify.infrastructure.crud.genre.dto.response;

import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;

import java.util.List;

public record GenreWithSongsResponseDto(String message, GenreDto genreDto, List<SongDto> songs) {
}
