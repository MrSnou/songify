package com.songify.domain.crud;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface GenreRepository extends CrudRepository<Genre, Long> {
    Genre save(Genre genre);

    @Query("SELECT g FROM Genre g WHERE g.id = :genreId")
    Optional<Genre> findById(Long genreId);

    @Modifying
    @Query("DELETE FROM Genre g WHERE g.id = :id")
    void deleteById(Long id);

    boolean existsGenreById(final Long genreId);
}
