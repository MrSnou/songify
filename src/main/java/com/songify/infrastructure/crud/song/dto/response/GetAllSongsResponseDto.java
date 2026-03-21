package com.songify.infrastructure.crud.song.dto.response;

import com.songify.infrastructure.crud.song.util.SongDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAllSongsResponseDto(List<SongDto> songs) {
}
