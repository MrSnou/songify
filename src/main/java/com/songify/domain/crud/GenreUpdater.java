package com.songify.domain.crud;

import com.songify.infrastructure.crud.genre.GenreDto;
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
//        oldGenre.changeGenreName(newName); // Hibernate Dirty Checking

        genreRepository.updateGenreById(genreId, newName);
        return new GenreDto(genreId, genre.getName());
    }
}
