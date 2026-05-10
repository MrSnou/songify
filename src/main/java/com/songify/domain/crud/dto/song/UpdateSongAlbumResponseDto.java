package com.songify.domain.crud.dto.song;


import com.songify.domain.crud.dto.album.AlbumDtoWithArtistsAndSongsResponseDto;

public record UpdateSongAlbumResponseDto(String message, AlbumDtoWithArtistsAndSongsResponseDto albumDto) {
}
