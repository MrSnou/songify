package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface AlbumRepository extends Repository<Album, Long> {

    Album save(final Album album);

    Optional<Album> findById(final Long id);

    Set<Album> findAll(Pageable pageable);

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

    List<Album> findBySongsId(Long songId);

}
