package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class ArtistDeleter {

    private final ArtistRepository artistRepository;
    private final ArtistRetriever artistRetriever;
    private final AlbumDeleter albumDeleter;
    private final SongDeleter songDeleter;

    private final AlbumRetriever albumRetriever;

    void deleteArtistByIdWithAlbumsAndSongs(final Long artistId) {
        Artist artist = artistRetriever.findArtistById(artistId);

        Set<Album> albums = new HashSet<>(artist.getAlbums());

        for (Album album : albums) {
            // Check if album is explicit to one artist if true - Continue.
            if (album.getArtists().size() == 1) {

                Set<Song> songsToDelete = new HashSet<>(album.getSongs());

                songsToDelete.forEach(song -> { // Get all songs from album before removal
                    songDeleter.deleteSongById(song.getId());   // Delete all songs before AlbumNotEmptyException.class
                });

                album.getSongs().clear();       // Clear Hibernate session cache memo

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

// TODO - Fix hermetic error (Domain Driven Design) / Refactor code to entities to not broke Service-Doman connection

//    void CC_DDD_Correct_Approach(final Long artistId) {
//        Artist artist = artistRetriever.findArtistById(artistId);
//        Set<Album> artistAlbums = albumRetriever.findAlbumsByArtistId(artist.getId());
//        if (artistAlbums.isEmpty()) {
//            artistRepository.deleteById(artistId);
//            return;
//        }
//
//        artistAlbums.stream()
//                .filter(album -> album.getArtists().size() >= 2)
//                .forEach(album -> album.removeArtist(artist));
//
//        Set<Album> albumWithOnlyOneArtist = artistAlbums.stream()
//                .filter(album -> album.getArtists().size() == 1)
//                .collect(Collectors.toSet());
//
//        Set<Long> allSongsFromAllAlbumsWhereWasOnlyThisArtist =
//                albumWithOnlyOneArtist.stream()
//                        .flatMap(album -> album.getSongs().stream())
//                        .map(Song::getId)
//                        .collect(Collectors.toSet());
//
//        Set<Long> albumsToDelete = albumWithOnlyOneArtist.stream()
//                .map(album -> album.getId())
//                .collect(Collectors.toSet());
//
//        songDeleter.deleteAllSongsById(allSongsFromAllAlbumsWhereWasOnlyThisArtist);
//        albumDeleter.deleteAllAlbumsByIds(albumsToDelete);
//        artistRepository.deleteArtistById(artistId);
//    }
}
