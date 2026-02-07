package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class ArtistUpdater {

    private final ArtistRetriever artistRetriever;

    // Dirty Checking by Hibernate (Can use raw JPQL in ArtistRepository)
    ArtistDto updateArtistNameById(final Long artistId, final String newName) {
        Artist artist = artistRetriever.findArtistById(artistId);
        artist.setName(newName);
        return new ArtistDto(artistId, artist.getName());
    }
}
