package com.songify.domain.crud;

import com.songify.infrastructure.crud.artist.ArtistDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
class ArtistUpdater {

    private final ArtistRetriever artistRetriever;

    ArtistDto updateArtistNameById(final Long artistId, final String newName) {
        Artist artist = artistRetriever.findArtistById(artistId);
        artist.setName(newName);
        return new ArtistDto(artistId, artist.getName());
    }
}
