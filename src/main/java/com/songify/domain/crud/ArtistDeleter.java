package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
@Transactional
class ArtistDeleter {

    private final ArtistRepository artistRepository;
    private final SongDeleter songDeleter;
    private final AlbumDeleter albumDeleter;
    private final ArtistRetriever artistRetriever;


    void deleteArtistById(final Long artistId) {
        Artist artist = artistRetriever.findArtistById(artistId);

        Set<Album> albums = new HashSet<>(artist.getAlbums());

        for  (Album album : albums) {
            // Check if album is explicit to one artist if true - Continue.
            if (album.getArtists().size() == 1) {

                Set<Song> songsToDelete = new HashSet<>(album.getSongs());

                songsToDelete.forEach(song -> { // Get all songs from album before removal
                    songDeleter.deleteSongById(song.getId());   // Delete all songs before AlbumNotEmptyException.class
                });

                album.getSongs().clear();       // Clear Hibernate session cache memo

                album.getSongs().clear();

                artist.getAlbums().remove(album);
                album.getArtists().remove(artist);

                albumDeleter.deleteById(album); // Delete empty album and artist

            } else {
                album.getArtists().remove(artist);
                artist.getAlbums().remove(album);
            }

        }
        artistRepository.deleteArtistById(artist.getId());

    }
}
