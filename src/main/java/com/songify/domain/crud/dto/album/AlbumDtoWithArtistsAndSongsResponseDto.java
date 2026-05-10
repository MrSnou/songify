package com.songify.domain.crud.dto.album;

import com.songify.domain.crud.dto.artist.ArtistDto;
import com.songify.domain.crud.dto.song.SongDto;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record AlbumDtoWithArtistsAndSongsResponseDto(

        Long id,
        String title,
        Instant releaseDate,
        Set<ArtistDto> artists,
        Set<SongDto> songs
)


{}
