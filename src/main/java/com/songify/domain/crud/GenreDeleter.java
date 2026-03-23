package com.songify.domain.crud;

import com.songify.domain.crud.model.DomainConstants;
import com.songify.infrastructure.crud.genre.error.GenreDefaultIsLockedException;
import com.songify.infrastructure.crud.genre.error.GenreIsUsedBySongsException;
import com.songify.infrastructure.crud.genre.error.GenreNotFoundException;
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
        Genre requestedGenreToDelete = genreRepository.findById(genre.getId())
                .orElseThrow(() -> new GenreNotFoundException("Genre with id:" + genre.getId() + "not found."));

        if (requestedGenreToDelete.getId().equals(DomainConstants.DEFAULT_GENRE_ID)) {
            throw new GenreDefaultIsLockedException("Cannot delete Default Genre from db!");
        }

        Set<Song> allSongsByGenre = songRetriever.getAllSongsByGenre(genre);
        if (allSongsByGenre.isEmpty()) {
            genreRepository.deleteById(genre.getId());
        } else {
            throw new GenreIsUsedBySongsException("Cannot delete genre, because it is used by songs.");
        }

    }
}
