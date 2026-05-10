package com.songify.domain.crud;


import com.songify.domain.crud.model.DomainConstants;
import com.songify.domain.crud.dto.genre.GenreDto;
import com.songify.domain.crud.dto.song.SongRequestDto;
import com.songify.domain.crud.dto.song.UpdateSongRequestDto;
import com.songify.domain.crud.dto.song.UpdateSongAlbumResponseDto;
import com.songify.domain.crud.dto.song.UpdateSongResponseDto;
import com.songify.domain.crud.exception.SongNotFoundException;
import com.songify.domain.crud.dto.song.SongDto;
import com.songify.domain.crud.dto.song.SongLanguageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongService {

    private final SongRepository songRepository;
    private final AlbumService albumService;
    private final GenreService genreService;

    SongDto addSong(final SongRequestDto requestDto) {
        SongLanguageDto language = requestDto.language();
        SongLanguage songLanguage = SongLanguage.valueOf(language.name());
        Genre defaultGenre = genreService.findGenreById(DomainConstants.DEFAULT_GENRE_ID);

        Song save = new Song(requestDto.name(), requestDto.releaseDate(), requestDto.duration(),  songLanguage, defaultGenre);
        Song saved = songRepository.save(save);
        return new SongDto(saved.getId(), saved.getName(), saved.getDuration(), saved.getReleaseDate(),
                new GenreDto(saved.getGenre().getId(), saved.getGenre().getName()));
    }

    void deleteSongById(Long id) {
        Song song = findSongById(id);
        albumService.deleteSongFromAlbums(song);
        songRepository.deleteById(song.getId());
    }

    List<SongDto> findAll(Pageable pageable) {
        return songRepository.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .duration(song.getDuration())
                        .releaseDate(song.getReleaseDate())
                        .genre(GenreDto.builder()
                                .id(song.getGenre().getId())
                                .name(song.getGenre().getName())
                                .build())
                        .build())
                .toList();
    }

    void checkIfExists(Long id) {
        if (!songRepository.existsById(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found.");
        }
    }

    Song findSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id: " + id + " not found."));
    }

    SongDto findSongDtoById(Long id) {
        Song songById = findSongById(id);
        Genre genreFromDb = genreService.findGenreById(songById.getGenre().getId());
        GenreDto genreDtoById = new GenreDto(genreFromDb.getId(), genreFromDb.getName());
        return SongDto.builder()
                .id(songById.getId())
                .name(songById.getName())
                .duration(songById.getDuration())
                .genre(genreDtoById)
                .build();
    }

    List<SongDto> findSongsDtoByGenreId(final Genre genre) {
        genreService.findGenreById(genre.getId());
        return songRepository.findAllByGenre(genre)
                .stream()
                .map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(), song.getReleaseDate(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .toList();
    }

    /// Song Updater
    UpdateSongResponseDto updateById(Long songId, UpdateSongRequestDto songFromRequest) {
        Song oldSong = findSongById(songId);

        String name = (songFromRequest.songName() != null) ? songFromRequest.songName() : oldSong.getName();
        Long duration = (songFromRequest.duration() != null) ? songFromRequest.duration() : oldSong.getDuration();

        Song updatedSong = new Song(
                name,
                oldSong.getReleaseDate(),
                duration, oldSong.getLanguage(),
                oldSong.getGenre());
        songRepository.updateById(songId, updatedSong);

        return new UpdateSongResponseDto(
                "Successfully updated song with id: " + songId +
                        " song title from " + oldSong.getName() + " to " + songFromRequest.songName() +
                        " and duration from " + oldSong.getDuration() + " to " + songFromRequest.duration() + "."
                , new SongDto(oldSong.getId(), songFromRequest.songName(), songFromRequest.duration(), oldSong.getReleaseDate(), new GenreDto(oldSong.getGenre().getId(), oldSong.getGenre().getName()))
        );
    }

    UpdateSongAlbumResponseDto updateSongAlbumById(final Long songId, final Long albumId) {
        return albumService.addSongToAlbum(songId, albumId);
    }

    UpdateSongResponseDto updateSongGenreById(final Long songId, final Long genreId) {
        checkIfExists(songId);

        Genre newGenre = genreService.findGenreById(genreId);

        songRepository.updateSongGenreById(songId, newGenre);
        SongDto updatedSongDto = findSongDtoById(songId);

        UpdateSongResponseDto response = UpdateSongResponseDto.builder()
                .message("Successfully updated song genre with id: " + songId + " to " +  newGenre.getName() + ".")
                .updatedSong(updatedSongDto)
                .build();

        return response;
    }
}
