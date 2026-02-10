package com.songify.domain.crud.dto;

import java.util.Set;

public record UpdateAlbumWithSongsAndArtistsResponseDto(String title, Set<ArtistDto> artists, Set<SongDto> songs) {
}
