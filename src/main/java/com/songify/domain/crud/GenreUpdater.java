package com.songify.domain.crud;

import com.songify.domain.crud.model.DomainConstants;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.error.GenreDefaultIsLockedException;
import com.songify.infrastructure.crud.genre.error.GenreNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class GenreUpdater {

    private final GenreRepository genreRepository;

    GenreDto updateGenreNameById(final Long genreId, String newName) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found."));

        if (genre.getId().equals(DomainConstants.DEFAULT_GENRE_ID)) {
            throw new GenreDefaultIsLockedException("Cannot edit Default Genre name!");
        }

        Genre updatedGenre = genreRepository.updateGenreById(genreId, newName);
        return new GenreDto(updatedGenre.getId(), updatedGenre.getName());
    }
}
