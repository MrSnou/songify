package com.songify.domain.crud.dto.genre;

import com.songify.domain.crud.dto.song.SongDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GenreWithSongsResponseDto(GenreDto genreDto, List<SongDto> songs) {
}
