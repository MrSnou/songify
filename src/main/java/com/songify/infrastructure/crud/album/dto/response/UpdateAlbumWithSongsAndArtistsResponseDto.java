package com.songify.infrastructure.crud.album.dto.response;

import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.artist.ArtistDto;

import java.util.Set;

public record UpdateAlbumWithSongsAndArtistsResponseDto(String title, Set<ArtistDto> artists, Set<SongDto> songs) {
}
