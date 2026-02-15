package com.songify.infrastructure.crud.song.dto.response;

import com.songify.infrastructure.crud.genre.GenreDto;

public record SongGenreDto(Long songId, GenreDto genre) {
}
