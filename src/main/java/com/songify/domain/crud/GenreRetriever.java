package com.songify.domain.crud;

import com.songify.domain.crud.Exceptions.GenreNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class GenreRetriever {

    private final GenreRepository genreRepository;


    Genre findGenreById(final Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found"));
    }
}
