package com.songify.infrastructure.crud.album.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateAlbumWithSongsAndArtistsRequestDto(String newName, List<Long> songIds, List<Long> artistIds) {
}
