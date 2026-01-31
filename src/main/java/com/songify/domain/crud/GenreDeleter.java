package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
@Log4j2
class GenreDeleter {

    private final GenreRepository genreRepository;

    void deleteGenreById(final Long genreId) {
        log.info("Deleting genre with id {}", genreId);
        genreRepository.deleteById(genreId);
    }
}
