package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

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

    Set<Album> findByArtistsId(Long artistId);

    @Query("""
            SELECT a FROM Album a
            """)
    Set<Album> findAllAlbums(Pageable pageable);


/**
 * Tried to use Interface with projection, but it didn't work, returned to old fashion DTO way.
 * Later to experiment with, but still managed to fetch it in one go. :)
 */
//    @Query("select a from Album a where a.id = :id")
//    Optional<AlbumInfo> findAlbumByIdWithSongsAndArtists(@Param("id") Long id);
}
