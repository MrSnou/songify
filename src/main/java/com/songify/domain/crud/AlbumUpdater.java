package com.songify.domain.crud;

import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.album.dto.request.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.infrastructure.crud.album.error.AlbumNotEmptyException;
import com.songify.infrastructure.crud.album.error.AlbumNotFoundException;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
class AlbumUpdater {

    private final AlbumRepository albumRepository;
    private final ArtistRetriever artistRetriever;
    private final SongRetriever songRetriever;


    void updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {

        Album oldAlbum = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotEmptyException("Album with id " + albumId + " does not exist"));

        requestDto.artistIds().forEach(artistId -> {
            Artist artist = artistRetriever.findArtistById(artistId);
            artist.getAlbums().add(oldAlbum);
        });

        requestDto.songIds().forEach(songId -> {
            Song song = songRetriever.findSongById(songId);
            oldAlbum.addSong(song);
        });

        oldAlbum.updateName(requestDto.newName());

        albumRepository.save(oldAlbum);
    }

    UpdateSongAlbumResponseDto addSongToAlbum(final Long songId, final Long albumId) {
        Album fetchedAlbum = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + albumId + " not found."));
        fetchedAlbum.addSongToAlbum(songRetriever.findSongById(songId));
        Album savedAlbum = albumRepository.save(fetchedAlbum);

        return new UpdateSongAlbumResponseDto("Successfully added song with id: " + songId + " to album with id: " + albumId +"."
                ,new UpdateAlbumWithSongsAndArtistsResponseDto(savedAlbum.getTitle(),
                savedAlbum.getArtists().stream()
                        .map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                        .collect(Collectors.toSet()),
                savedAlbum.getSongs().stream()
                        .map(song -> new SongDto(song.getId(), song.getName(), song.getDuration()))
                        .collect(Collectors.toSet())
        ));
    }

    void deleteSongFromAlbums(Song song) {

        List<Album> albums = albumRepository.findBySongsId(song.getId());

        for (Album album : albums) {
            album.removeSong(song);
        }
    }
}
