package com.songify.infrastructure.crud.album.dto.response;

import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.song.util.SongInfoDto;

import java.time.Instant;
import java.util.Set;

public record AlbumDtoWithArtistsAndSongsResponseDto(

        Long id,
        String name,
        Instant releaseDate,
        Set<ArtistDto> artists,
        Set<SongInfoDto> songs
)


{}
