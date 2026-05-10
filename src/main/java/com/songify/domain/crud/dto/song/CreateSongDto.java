package com.songify.domain.crud.dto.song;

import lombok.Builder;

@Builder
public record CreateSongDto(SongDto song) {
}
