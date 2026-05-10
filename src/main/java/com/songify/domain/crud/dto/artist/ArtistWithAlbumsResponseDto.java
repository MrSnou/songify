package com.songify.domain.crud.dto.artist;

import com.songify.domain.crud.dto.album.AllAlbumsResponseDto;

public record ArtistWithAlbumsResponseDto(String message, ArtistDto artistDto, AllAlbumsResponseDto allAlbumsResponseDto) {
}
