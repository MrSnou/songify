package com.songify.infrastructure.crud.song.controller.dto.response;

import com.songify.domain.crud.dto.GenreDto;

public record SongGenreDto(Long songId, GenreDto genre) {
}
