package com.songify.domain.crud;


import com.songify.domain.crud.model.DomainConstants;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.domain.crud.exception.GenreNotFoundException;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongResponseDto;
import com.songify.domain.crud.exception.SongNotFoundException;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongLanguageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongService {

    private final SongRepository songRepository;
    private final GenreRepository genreRepository;
    private final AlbumService albumService;

    SongDto addSong(final SongRequestDto requestDto) {
        SongLanguageDto language = requestDto.language();
        SongLanguage songLanguage = SongLanguage.valueOf(language.name());
        Genre defaultGenre = getGenreFromDb(DomainConstants.DEFAULT_GENRE_ID);

        Song save = new Song(requestDto.name(), requestDto.releaseDate(), requestDto.duration(),  songLanguage, defaultGenre);
        Song saved = songRepository.save(save);
        return new SongDto(saved.getId(), saved.getName(), saved.getDuration(), new GenreDto(saved.getGenre().getId(), saved.getGenre().getName()));
    }

    DeleteSongResponseDto deleteSongById(Long id) {
        Song song = findSongById(id);

        albumService.deleteSongFromAlbums(song);

        songRepository.deleteById(song.getId());

        DeleteSongResponseDto responseDto = DeleteSongResponseDto.builder()
                .message("Song with id " + song.getId() + " was deleted.")
                .status(HttpStatus.OK)
                .build();

        return responseDto;
    }

    List<SongDto> findAll(Pageable pageable) {
        return songRepository.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .duration(song.getDuration())
                        .genre(GenreDto.builder()
                                .id(song.getGenre().getId())
                                .name(song.getGenre().getName())
                                .build())
                        .build())
                .toList();
    }

    void checkIfExists(Long id) {
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
        Genre genreFromDb = getGenreFromDb(songById.getGenre().getId());
        GenreDto genreDtoById = new GenreDto(genreFromDb.getId(), genreFromDb.getName());
        return SongDto.builder()
                .id(songById.getId())
                .name(songById.getName())
                .duration(songById.getDuration())
                .genre(genreDtoById)
                .build();
    }

    SongGenreDto findSongGenreDtoById(final Long id) {
        Song fetchedSong = findSongById(id);

        if (fetchedSong.getGenre() == null) {
            Genre defaultGenre = getGenreFromDb(DomainConstants.DEFAULT_GENRE_ID);
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

        UpdateSongResponseDto response = new UpdateSongResponseDto(
                "Successfully updated song with id: " + songId +
                        " song name from " + oldSong.getName() + " to " + songFromRequest.songName() +
                        " and duration from " + oldSong.getDuration() + " to " + songFromRequest.duration() + "."
                , new SongDto(oldSong.getId(), songFromRequest.songName(), songFromRequest.duration(), new GenreDto(oldSong.getGenre().getId(), oldSong.getGenre().getName()))
        );

        return response;
    }

    UpdateSongAlbumResponseDto updateSongAlbumById(final Long songId, final Long albumId) {
        return albumService.addSongToAlbum(songId, albumId);
    }

    UpdateSongResponseDto updateSongGenreById(final Long songId, final Long genreId) {
        checkIfExists(songId);

        Genre newGenre = getGenreFromDb(genreId);

        songRepository.updateSongGenreById(songId, newGenre);
        SongDto updatedSongDto = findSongDtoById(songId);

        UpdateSongResponseDto response = UpdateSongResponseDto.builder()
                .message("Successfully updated song genre with id: " + songId + " to " +  newGenre.getName() + ".")
                .updatedSong(updatedSongDto)
                .build();

        return response;
    }

    private Genre getGenreFromDb(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " not found."));
    }
}
