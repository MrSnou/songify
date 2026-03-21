package com.songify.infrastructure.crud.song.dto.response;


import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import lombok.Builder;

@Builder
public record SongWithGenreResponseDto(SongDto song, GenreDto genre) {
}
