package com.songify.infrastructure.crud.song.dto.response;


import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;

public record GetSongResponseDto(SongDto song, GenreDto genre) {
}
