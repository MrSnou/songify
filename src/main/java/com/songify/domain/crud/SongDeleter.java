package com.songify.domain.crud;

import com.songify.infrastructure.crud.song.error.SongNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final AlbumRetriever albumRetriever;

    void deleteSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));

        albumUpdater.deleteSongFromAlbums(song);

        songRepository.deleteById(song.getId());
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
