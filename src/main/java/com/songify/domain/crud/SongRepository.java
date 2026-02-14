package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface SongRepository extends Repository<Song, Long> {

    Song save(Song song);

    @Query(value = """
            SELECT s FROM Song s
            JOIN FETCH s.genre
            """)
    List<Song> findAll(Pageable pageable);

    @Query("SELECT s FROM Song s WHERE s.id = :id")
    Optional<Song> findById(Long id);

    @Modifying
    @Query("DELETE FROM Song s WHERE s.id = :id")
    void deleteById(Long id);

    @Modifying
//    @Query("UPDATE Song s SET s.name = :#{#newSong.name} WHERE s.id = :id")
    @Query("UPDATE Song s SET s.name = :#{#newSong.name}, s.duration = :#{#newSong.duration} WHERE s.id = :id")
    void updateById(Long id, Song newSong);

    boolean existsById(Long id);

    Set<Song> findAllByGenre(Genre genre);

    @Transactional
    @Modifying
    @Query("UPDATE Song s SET s.genre = :genre WHERE s.id = :id")
    void updateSongGenreById(@Param("id") Long id, @Param("genre") Genre genre);
}
