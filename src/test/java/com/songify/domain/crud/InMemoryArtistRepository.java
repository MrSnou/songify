package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemoryArtistRepository implements ArtistRepository {

    HashMap<Long, Artist> artistsDb =  new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public Artist save(final Artist artist) {
        long index = this.index.getAndIncrement();
        artistsDb.put(index, artist);
        artist.setId(Long.valueOf(index));
        return artist;
    }

    @Override
    public Set<Artist> findAll(final Pageable pageable) {
        return new HashSet<>(artistsDb.values());
    }

    @Override
    public Optional<Artist> findArtistById(final Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteArtistById(final Long artistId) {

    }

    @Override
    public boolean existsById(final Long id) {
        return false;
    }
}
