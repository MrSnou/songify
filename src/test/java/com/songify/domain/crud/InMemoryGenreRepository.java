package com.songify.domain.crud;

import com.songify.infrastructure.crud.genre.GenreDto;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryGenreRepository implements GenreRepository {

    Map<Long, Genre> db =  new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public Genre save(final Genre genre) {
        long index = this.index.getAndIncrement();
        db.put(index, genre);
        genre.setId(Long.valueOf(index));
        return genre;
    }

    @Override
    public Optional<Genre> findById(final Long genreId) {
        return Optional.ofNullable(db.get(genreId));
    }

    @Override
    public Iterable<Genre> findAll() {
        return new HashSet<Genre>(db.values());
    }

    @Override
    public <S extends Genre> Iterable<S> saveAll(final Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
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
    public List<Genre> findAll(final Pageable pageable) {
        return List.of();
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
