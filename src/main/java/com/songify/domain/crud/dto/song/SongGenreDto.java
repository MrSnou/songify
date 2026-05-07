package com.songify.domain.crud.dto.song;

import com.songify.domain.crud.dto.genre.GenreDto;

public record SongGenreDto(Long songId, GenreDto genre) {
}
