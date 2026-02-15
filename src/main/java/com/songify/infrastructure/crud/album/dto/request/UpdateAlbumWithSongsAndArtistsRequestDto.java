package com.songify.infrastructure.crud.album.dto.request;

import java.util.List;

public record UpdateAlbumWithSongsAndArtistsRequestDto(String newName, List<Long> songIds, List<Long> artistIds) {
}
