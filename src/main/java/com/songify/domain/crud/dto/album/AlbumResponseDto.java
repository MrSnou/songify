package com.songify.domain.crud.dto.album;

import lombok.Builder;

@Builder
public record AlbumResponseDto(String message, AlbumDtoWithArtistsAndSongsResponseDto album) {
}
