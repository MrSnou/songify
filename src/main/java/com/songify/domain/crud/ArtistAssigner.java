package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class ArtistAssigner {

    private final AlbumRetriever albumRetriever;
    private final ArtistRetriever artistRetriever;

    void addArtistToAlbum(final Long artistID, final Long albumID) {
        Artist artist = artistRetriever.findArtistById(artistID);
        Album album = albumRetriever.findAlbumById(albumID);
        artist.addAlbum(album);
    }
}
