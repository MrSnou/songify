package com.songify.infrastructure.crud.song.dto.response;

import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;

public record UpdateSongAlbumResponseDto(String message, UpdateAlbumWithSongsAndArtistsResponseDto albumDto) {
}
