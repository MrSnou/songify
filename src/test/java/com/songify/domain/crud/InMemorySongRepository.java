package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class InMemorySongRepository implements SongRepository {
    Map<Long, Song> db =  new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public Song save(final Song song) {
        long index = this.index.getAndIncrement();
        db.put(index, song);
        song.setId(Long.valueOf(index));
        return song;
    }

    @Override
    public List<Song> findAll(final Pageable pageable) {
        return List.of();
    }

    @Override
    public Optional<Song> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public void updateById(final Long id, final Song newSong) {

    }

    @Override
    public boolean existsById(final Long id) {
        return false;
    }

    @Override
    public Set<Song> findAllByGenre(final Genre genre) {
        return Set.of();
    }

    @Override
    public void updateSongGenreById(final Long id, final Genre genre) {

    }
}
