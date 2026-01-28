package com.songify.domain.crud.dto;

import java.time.Instant;
import java.util.Set;

public record AlbumDtoWithArtistsAndSongs(

        Long id,
        String name,
        Instant releaseDate,
        Set<ArtistInfoDto> artists,
        Set<SongInfoDto> songs
)


{}
