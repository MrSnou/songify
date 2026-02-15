package com.songify.infrastructure.crud.song.dto.response;

import com.songify.infrastructure.crud.song.util.SongDto;

import java.util.List;

public record GetAllSongsResponseDto(List<SongDto> songs) {
}
