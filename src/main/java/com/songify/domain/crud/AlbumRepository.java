package com.songify.domain.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface AlbumRepository extends JpaRepository<Album, Long> {

    Album save(final Album album);

// TODO - Fix performence with db
    Optional<Album> findById(final Long id);

// Problem when using query higher than 9 (working only with 1-9 id of the album)
    @Query("""
            select distinct a from Album a
            join fetch a.songs songs
                join fetch songs.genre genre
            join fetch a.artists artists
            where a.id = :id
                        """)
    Optional<Album> findAlbumByIdWithSongsAndArtists(@Param("id") final Long id);

/**
 * Tried to use Interface with projection, but it didn't work, returned to old fashion DTO way.
 * Later to experiment with, but still managed to fetch it in one go. :)
 */
//    @Query("select a from Album a where a.id = :id")
//    Optional<AlbumInfo> findAlbumByIdWithSongsAndArtists(@Param("id") Long id);
}
