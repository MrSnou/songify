package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class InMemorySongRepository implements SongRepository {
    @Override
    public Song save(final Song song) {
        return null;
    }

    @Override
    public List<Song> findAll(final Pageable pageable) {
        return List.of();
    }

    @Override
    public Optional<Song> findById(final Long id) {
        return Optional.empty();
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
