package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemoryAlbumRepository implements AlbumRepository {

    Map<Long, Album> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public Album save(final Album album) {
        long index = this.index.getAndIncrement();
        db.put(index, album);
        album.setId(Long.valueOf(index));
        return album;
    }

    @Override
    public Optional<Album> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Album> findAll(Pageable pageable) {
        return db.values().stream().collect(Collectors.toSet());
    }

    @Override
    public Optional<Album> findAlbumByIdWithSongsAndArtists(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Album> findByArtistsId(final Long artistId) {
        return db.values().stream()
                .filter(album -> album.getArtists().stream()
                        .anyMatch(artist -> artist.getId().equals(artistId)))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteById(final Long id) {
        db.remove(id);
    }

    @Override
    public List<Album> findBySongsId(final Long songId) {
        List<Album> albumsWithSong = new ArrayList<Album>();
        List<Album> albums = db.values().stream().toList();
        for (Album album : albums) {
            if (album.getSongs().stream().anyMatch(song -> song.getId().equals(songId))) {
                albumsWithSong.add(album);
            }
        }
        return albumsWithSong;
    }
}
