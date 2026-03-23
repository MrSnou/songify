package com.songify.domain.crud;

import com.songify.domain.crud.model.DomainConstants;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.dto.response.SongWithGenreResponseDto;
import com.songify.infrastructure.crud.song.error.SongNotFoundException;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Log4j2
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongRetriever {

    private final SongRepository songRepository;
    private final GenreRetriever genreRetriever;

    List<SongDto> findAll(Pageable pageable) {
        return songRepository.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .duration(song.getDuration())
                        .build())
                .toList();
    }

    void existsById(Long id) {
        if (!songRepository.existsById(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
    }

    Song findSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
    }

    SongDto findSongDtoById(Long id) {
        Song songById = findSongById(id);
        return SongDto.builder()
                .id(songById.getId())
                .name(songById.getName())
                .duration(songById.getDuration())
                .build();
    }


    Set<Song> getAllSongsByGenre(final Genre genre) {
        Set<Song> songs = songRepository.findAllByGenre(genre);
        return songs;
    }

    SongGenreDto findSongGenreDtoById(final Long id) {
        Song fetchedSong = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));

        if (fetchedSong.getGenre() == null) {
            Genre defaultGenre = genreRetriever.findGenreById(DomainConstants.DEFAULT_GENRE_ID);
            fetchedSong.setGenre(defaultGenre);
        }

        GenreDto genreDto = new GenreDto(fetchedSong.getGenre().getId(),  fetchedSong.getGenre().getName());

        return new SongGenreDto(fetchedSong.getId(), genreDto);

    }

    List<SongDto> findSongsDtoByGenreId(final Genre genre) {
        return songRepository.findAllByGenre(genre)
                .stream()
                .map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .toList();
    }

    SongWithGenreResponseDto findSongDtoWithGenreDtoById(final Long songId) {
        Song songById = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song with id: " + songId + " not found."));
        GenreDto genreDto = genreRetriever.findGenreDtoById(songById.getGenre().getId());
        return SongWithGenreResponseDto.builder()
                .song(findSongDtoById(songId))
                .genre(genreDto)
                .build();
    }
}
