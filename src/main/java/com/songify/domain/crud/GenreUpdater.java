package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.exceptions.GenreNotFoundException;
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
