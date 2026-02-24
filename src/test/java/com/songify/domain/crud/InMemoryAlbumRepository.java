package com.songify.domain.crud;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

class InMemoryAlbumRepository implements AlbumRepository {

    Map<Long, Album> db =  new HashMap<>();
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
    public boolean existsById(final Long aLong) {
        return false;
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
    public void delete(final Album entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends Album> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Album> S saveAndFlush(final S entity) {
        return null;
    }

    @Override
    public <S extends Album> List<S> saveAllAndFlush(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(final Iterable<Album> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(final Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Album getOne(final Long aLong) {
        return null;
    }

    @Override
    public Album getById(final Long aLong) {
        return null;
    }

    @Override
    public Album getReferenceById(final Long aLong) {
        return null;
    }

    @Override
    public <S extends Album> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Album> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Album> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Album> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Album> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Album> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends Album, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Album> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Album> findAll() {
        return List.of();
    }

    @Override
    public List<Album> findAllById(final Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<Album> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<Album> findAll(final Pageable pageable) {
        return null;
    }
}
