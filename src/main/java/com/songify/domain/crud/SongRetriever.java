package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.exceptions.SongNotFoundException;
import com.songify.domain.crud.dto.SongDto;
import com.songify.infrastructure.crud.song.controller.dto.response.SongGenreDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongRetriever {
    private final SongRepository songRepository;
    private final GenreRetriever genreRetriever;

    List<SongDto> findAll(Pageable pageable) {
        log.info("Retrievind all songs : ");
        return songRepository.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .name(song.getName())
                        .build())
                .toList();
    }

    void existsById(Long id) {
        log.info("Checking if song with id exists: " + id);
        if (!songRepository.existsById(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
    }

    Song findSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
    }

    SongDto findSongDtoById(Long id) {
        return songRepository.findById(id)
                .map(song ->
                    SongDto.builder()
                            .id(song.getId())
                            .name(song.getName())
                            .duration(song.getDuration())
                            .build()
                )
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
    }


    Set<Song> getAllSongsByGenre(final Genre genre) {
        Set<Song> songs = songRepository.findAllByGenre(genre);
        return songs;
    }

    SongGenreDto findSongGenreDtoById(final Long id) {
        Song fetchedSong = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));

        if (fetchedSong.getGenre() == null) {
            Genre defaultGenre = genreRetriever.findGenreById(1L);
            fetchedSong.setGenre(defaultGenre);
        }

        GenreDto genreDto = new GenreDto(fetchedSong.getGenre().getId(),  fetchedSong.getGenre().getName());

        return new SongGenreDto(fetchedSong.getId(),  genreDto);

    }
}
