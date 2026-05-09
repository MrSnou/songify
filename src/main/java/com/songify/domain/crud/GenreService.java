package com.songify.domain.crud;

import com.songify.domain.crud.dto.genre.AllGenresDto;
import com.songify.domain.crud.dto.genre.GenreResponseDto;
import com.songify.domain.crud.model.DomainConstants;
import com.songify.domain.crud.dto.genre.GenreDto;
import com.songify.domain.crud.exception.GenreDefaultIsLockedException;
import com.songify.domain.crud.exception.GenreIsUsedBySongsException;
import com.songify.domain.crud.exception.GenreNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class GenreService {

    private final GenreRepository genreRepository;
    private final SongRepository songRepository;

    GenreDto addGenre(final String name) {
        Genre genre = new Genre(name);
        Genre save = genreRepository.save(genre);
        return new GenreDto(save.getId(), save.getName());
    }

    void deleteGenreById(final Genre genre) {

        if (genre.getId().equals(DomainConstants.DEFAULT_GENRE_ID)) {
            throw new GenreDefaultIsLockedException("Cannot delete Default Genre from db!");
        }

        Set<Song> allSongsByGenre = new HashSet<>(songRepository.findAllByGenre(genre));
        if (allSongsByGenre.isEmpty()) {
            genreRepository.deleteById(genre.getId());
        } else {
            throw new GenreIsUsedBySongsException("Cannot delete genre, because it is used by songs.");
        }
    }

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

    AllGenresDto findAllGenreDto(Pageable pageable) {
        List<Genre> genres = findAll(pageable);
        List<GenreDto> genreDtoList = genres
                .stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
        return AllGenresDto.builder().genres(genreDtoList).build();
    }

    GenreResponseDto updateGenreNameById(final Long genreId, String newName) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found."));

        if (genre.getId().equals(DomainConstants.DEFAULT_GENRE_ID)) {
            throw new GenreDefaultIsLockedException("Cannot edit Default Genre name!");
        }

        genreRepository.updateGenreById(genreId, newName);
        Genre updatedGenre = genreRepository.findById(genreId).orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found."));
        return GenreResponseDto.builder()
                .message("Genre with id " + genre.getId() + " name successfully updated from: " + genre.getName() + " to " + updatedGenre.getName() + ".")
                .build();
    }
}
