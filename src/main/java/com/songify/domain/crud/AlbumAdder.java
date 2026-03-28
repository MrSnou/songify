package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
class AlbumAdder {

    private final SongRetriever songRetriever;
    private final AlbumRepository albumRepository;

    AlbumDto addAlbumWithSong(final List<Long> songIds, final String title, final Instant releaseDate) {
        List<Song> songs = songIds.stream().map(songRetriever::findSongById).toList();

        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        songs.forEach(album::addSongToAlbum);
        Album savedAlbum = albumRepository.save(album);
        return new AlbumDto(savedAlbum.getId(),  savedAlbum.getTitle(),
                savedAlbum.getSongs().stream().map(song -> song.getId()).toList());

    }

    Album addAlbum(final String title, final Instant releaseDate) {
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        return albumRepository.save(album);
    }
}
