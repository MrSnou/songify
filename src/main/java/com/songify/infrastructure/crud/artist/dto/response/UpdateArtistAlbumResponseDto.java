package com.songify.infrastructure.crud.artist.dto.response;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.artist.ArtistDto;
import lombok.Builder;

@Builder
public record UpdateArtistAlbumResponseDto(String message, ArtistDto artist, AlbumDto album) {
}
