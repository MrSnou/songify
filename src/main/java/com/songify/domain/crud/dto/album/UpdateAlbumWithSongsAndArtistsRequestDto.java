package com.songify.domain.crud.dto.album;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateAlbumWithSongsAndArtistsRequestDto(String newName, List<Long> songIds, List<Long> artistIds) {
}
