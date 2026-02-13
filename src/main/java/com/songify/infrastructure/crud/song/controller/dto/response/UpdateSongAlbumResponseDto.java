package com.songify.infrastructure.crud.song.controller.dto.response;

import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsResponseDto;

public record UpdateSongAlbumResponseDto(String message, UpdateAlbumWithSongsAndArtistsResponseDto albumDto) {
}
