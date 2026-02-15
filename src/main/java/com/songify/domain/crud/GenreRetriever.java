package com.songify.domain.crud;

import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.error.GenreNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class GenreRetriever {

    private final GenreRepository genreRepository;


    Genre findGenreById(final Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found"));
    }

    GenreDto findGenreDtoById(final Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found"));
        return new GenreDto(genre.getId(), genre.getName());
    }

    List<Genre> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    List<GenreDto> findAllGenreDto(Pageable pageable) {
        List<Genre> genres = findAll(pageable);
        List<GenreDto> genreDtos = genres
                .stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
        return genreDtos;
    }
}
