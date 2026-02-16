package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

class InMemoryArtistRepository implements ArtistRepository {
    @Override
    public Artist save(final Artist artist) {
        return null;
    }

    @Override
    public Set<Artist> findAll(final Pageable pageable) {
        return Set.of();
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
