package com.songify.domain.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

interface AlbumRepository extends JpaRepository<Album, Long> {

    Album save(final Album album);

    Optional<Album> findById(final Long id);

    @Modifying
    void deleteById(final Long id);

    @Query("""
                select distinct a from Album a
                left join fetch a.artists ar
                left join fetch a.songs s
                left join fetch s.genre g
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
