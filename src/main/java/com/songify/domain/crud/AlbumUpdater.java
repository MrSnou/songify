package com.songify.domain.crud;

import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsDto;
import com.songify.domain.crud.exceptions.AlbumNotEmptyException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



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
}
