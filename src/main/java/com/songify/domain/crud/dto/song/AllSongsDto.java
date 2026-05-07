package com.songify.domain.crud.dto.song;

import lombok.Builder;

import java.util.List;

@Builder
public record AllSongsDto(List<SongDto> songs) {
}
