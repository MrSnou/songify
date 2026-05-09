package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryGenreRepository implements GenreRepository {

    Map<Long, Genre> db =  new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);



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
    public List<Genre> findAll(final Pageable pageable) {
        return new  ArrayList<>(db.values());
    }

    @Override
    public void deleteById(final Long id) {
        db.remove(id);
    }

    @Override
    public void updateGenreById(final Long genreId, final String newName) {
        Genre oldGenre = db.remove(genreId);
        oldGenre.setName(newName);
        db.put(oldGenre.getId(), oldGenre);
    }
}
