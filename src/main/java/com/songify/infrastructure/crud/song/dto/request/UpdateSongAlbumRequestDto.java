package com.songify.infrastructure.crud.song.dto.request;

import lombok.Builder;

@Builder
public record UpdateSongAlbumRequestDto(Long albumId) {
}
