package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsDto;
import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.domain.crud.exceptions.AlbumNotEmptyException;
import com.songify.domain.crud.exceptions.AlbumNotFoundException;
import com.songify.infrastructure.crud.song.controller.dto.response.UpdateSongAlbumResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
class AlbumUpdater {

    private final AlbumRepository albumRepository;
    private final ArtistRetriever artistRetriever;
    private final SongRetriever songRetriever;


    void updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsDto requestDto) {

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
}
