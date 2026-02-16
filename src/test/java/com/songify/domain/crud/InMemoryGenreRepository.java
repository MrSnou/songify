package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

class InMemoryGenreRepository implements GenreRepository {
    @Override
    public <S extends Genre> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public Iterable<Genre> findAll() {
        return null;
    }

    @Override
    public Iterable<Genre> findAllById(final Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(final Genre entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends Genre> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Genre save(final Genre genre) {
        return null;
    }

    @Override
    public List<Genre> findAll(final Pageable pageable) {
        return List.of();
    }

    @Override
    public Optional<Genre> findById(final Long genreId) {
        return Optional.empty();
    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public boolean existsGenreById(final Long genreId) {
        return false;
    }

    @Override
    public void updateGenreById(final Long genreId, final String newName) {

    }
}
