package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistWithAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.dto.response.UpdateArtistAlbumResponseDto;
import com.songify.domain.crud.exception.ArtistNotFoundException;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class ArtistService {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    ArtistDto addArtist(final String name) {
        Artist artist = new Artist(name);
        Artist save = artistRepository.save(artist);
        return new ArtistDto(save.getId(), save.getName());
    }

    UpdateArtistAlbumResponseDto addArtistToAlbum(final Long artistID, final Long albumID) {
        Artist artist = findArtistById(artistID);
        Album album = albumRepository.findById(albumID)
                .orElseThrow(() -> new ArtistNotFoundException("Album with id " + albumID + " not found."));
        artist.addAlbum(album);
        return UpdateArtistAlbumResponseDto.builder()
                .message("Successfully added artist [" + artist.getId() + "] " + artist.getName() +
                        " to album [" + album.getId() + "] " + album.getTitle() + ".")
                .artist(new ArtistDto(artist.getId(), artist.getName()))
                .album(new AlbumDto(album.getId(), album.getTitle(), album.getSongs()
                        .stream().map(
                                song -> new SongDto(song.getId(), song.getName(), song.getDuration(),
                                        new GenreDto(song.getGenre().getId(), song.getGenre().getName()))
                        ).toList()
                ))
                .build();
    }
    void deleteArtistByIdWithAlbumsAndSongs(final Long artistId) {
        Artist artist = findArtistById(artistId);

        Set<Album> albums = new HashSet<>(artist.getAlbums());

        for (Album album : albums) {
            // Check if album is explicit to one artist if true - Continue.
            if (album.getArtists().size() == 1) {

                Set<Song> songsToDelete = new HashSet<>(album.getSongs());

                songsToDelete.forEach(song -> { // Get all songs from album before removal
                    songRepository.deleteById(song.getId()); // Delete all songs before AlbumNotEmptyException.class
                });

                album.getSongs().clear();       // Clear Hibernate session cache memo

                artist.getAlbums().remove(album);
                album.getArtists().remove(artist);

                albumRepository.deleteById(album.getId()); // Delete empty album and artist

            } else {
                album.getArtists().remove(artist);
                artist.getAlbums().remove(album);
            }
        }
        artistRepository.deleteArtistById(artist.getId());
    }
    Set<ArtistDto> findAllArtists(final Pageable pageable) {
        return artistRepository.findAll(pageable)
                .stream()
                .map(artist -> new ArtistDto(
                        artist.getId(), artist.getName()
                ))
                .collect(Collectors.toSet());
    }

    Artist findArtistById(final Long artistId) {
        return artistRepository.findArtistById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found."));
    }

    ArtistWithAlbumsResponseDto findArtistWithAlbumsById(final Long artistId) {
        Artist artist = artistRepository.findArtistById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found."));

        List<AlbumDto> albumDtoList = artist.getAlbums()
                .stream()
                .filter(album -> album.getArtists().contains(artist))
                .map(album -> new AlbumDto(album.getId(), album.getTitle(),
                        album.getSongs()
                                .stream().map(
                                        song -> new SongDto(song.getId(), song.getName(), song.getDuration(),
                                                new GenreDto(song.getGenre().getId(), song.getGenre().getName()))
                                ).toList()))
                .toList();

        ArtistWithAlbumsResponseDto responseDto = new ArtistWithAlbumsResponseDto(
                "Successfully retrieved: " + artist.getName() + " with all it's albums.",
                new ArtistDto(artist.getId(), artist.getName()),
                new AllAlbumsResponseDto(albumDtoList)
        );

        return responseDto;
    }

    ArtistDto updateArtistNameById(final Long artistId, final String newName) {
        Artist artist = findArtistById(artistId);
        artist.setName(newName);
        return new ArtistDto(artistId, artist.getName());
    }
}
