package com.songify.domain.crud.dto.artist;

import com.songify.domain.crud.dto.album.AlbumDto;
import lombok.Builder;

@Builder
public record UpdateArtistAlbumResponseDto(String message, ArtistDto artist, AlbumDto album) {
}
