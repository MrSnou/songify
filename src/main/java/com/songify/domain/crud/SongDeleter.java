package com.songify.domain.crud;

import com.songify.infrastructure.crud.song.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.error.SongNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.sql.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongDeleter {

    private final SongRepository songRepository;
    private final AlbumUpdater albumUpdater;

    DeleteSongResponseDto deleteSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));

        albumUpdater.deleteSongFromAlbums(song);

        songRepository.deleteById(song.getId());

        DeleteSongResponseDto responseDto = DeleteSongResponseDto.builder()
                .message("Song with id " + song.getId() + " was deleted.")
                .status(HttpStatus.OK)
                .build();

        return responseDto;
    }

//    void deleteSongAndGenreById(final Long id) {
//        Song song = songRetriever.findSongById(id);
//        Long genreId = song.getGenre().getId();
//
//        deleteSongById(id);
//
//        songUpdater.findAllSongsByGenreId(genreId);
//        genreDeleter.deleteGenreById(genreId);
//    }
}
