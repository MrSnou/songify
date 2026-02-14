package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

interface GenreRepository extends CrudRepository<Genre, Long> {

    Genre save(Genre genre);

    @Query("SELECT g FROM Genre g")
    List<Genre> findAll(Pageable pageable);

    @Query("SELECT g FROM Genre g WHERE g.id = :genreId")
    Optional<Genre> findById(Long genreId);

    @Modifying
    @Query("DELETE FROM Genre g WHERE g.id = :id")
    void deleteById(Long id);

    boolean existsGenreById(final Long genreId);

    @Modifying
    @Transactional
    @Query("UPDATE Genre g set g.name = :newName where g.id = :genreId")
    void updateGenreById(final Long genreId, String newName);


}
