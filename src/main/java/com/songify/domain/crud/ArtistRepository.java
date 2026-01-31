package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface ArtistRepository extends Repository<Artist, Long> {
    Artist save(Artist artist);

    Set<Artist> findAll(Pageable pageable);

    Optional<Artist> findArtistById(Long id);

    @Modifying
    @Query("DELETE FROM Artist a WHERE a.id = :artistId")
    void deleteArtistById(Long artistId);

    boolean existsById(Long id);
}
