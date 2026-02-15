package com.songify.infrastructure.crud.artist.dto.response;

import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.ArtistDto;

public record ArtistWithAlbumsResponseDto(String message, ArtistDto artistDto, AllAlbumsResponseDto allAlbumsResponseDto) {
}
