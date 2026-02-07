package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
@Transactional
class AlbumAdder {

    private final SongRetriever songRetriever;
    private final AlbumRepository albumRepository;

    AlbumDto addAlbumWithSong(final Long songId, final String title, final Instant releaseDate) {
        Song retrievedSong = songRetriever.findSongById(songId);

        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.addSongToAlbum(retrievedSong);
        Album savedAlbum = albumRepository.save(album);
        return new AlbumDto(savedAlbum.getId(),  savedAlbum.getTitle());

    }

    Album addAlbum(final String title, final Instant releaseDate) {
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        return albumRepository.save(album);
    }
}
