package com.songify.domain.crud;

import com.songify.domain.crud.Exceptions.GenreIsUsedBySongsException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;


@AllArgsConstructor
@Service
@Log4j2
class GenreDeleter {

    private final GenreRepository genreRepository;
    private final SongRetriever songRetriever;

    void deleteGenreById(final Genre genre) {
        log.info("Deleting genre with id {}", genre.getId());
        Set<Song> allSongsByGenre = songRetriever.getAllSongsByGenre(genre);
        if (allSongsByGenre.isEmpty()) {
            genreRepository.deleteById(genre.getId());
        } else {
            throw new GenreIsUsedBySongsException("Cannot delete genre, because it is used by songs.");
        }

    }
}
