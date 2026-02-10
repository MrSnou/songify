package com.songify.domain.crud.dto;

import java.util.List;

public record UpdateAlbumWithSongsAndArtistsDto(String newName, List<Long> songIds, List<Long> artistIds) {
}
