package com.songify.domain.crud;

import com.songify.domain.crud.dto.SongDto;
import com.songify.infrastructure.crud.song.controller.dto.request.UpdateSongAlbumRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.response.UpdateSongAlbumResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongUpdater {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;
    private final AlbumUpdater albumUpdater;


    SongDto updateById(Long id, UpdateSongRequestDto songFromRequest) {
        Song oldSong = songRetriever.findSongById(id);

        String name = null;
        Long duration = null;
        if (songFromRequest.songName() != null) {
            name = songFromRequest.songName();
        } else {
            name = oldSong.getName();
        }

        if  (songFromRequest.duration() != oldSong.getDuration()) {
            duration = songFromRequest.duration();
        } else {
            duration = oldSong.getDuration();
        }

        Song updatedSong = new Song(name, oldSong.getReleaseDate(), duration, oldSong.getLanguage());
        songRepository.updateById(id, updatedSong);

        return new SongDto(oldSong.getId(), oldSong.getName(), oldSong.getDuration());
    }

    UpdateSongAlbumResponseDto updateSongAlbumById(final Long songId, final UpdateSongAlbumRequestDto request) {
        return albumUpdater.addSongToAlbum(songId, request.albumId());
    }

//    Song updatePartiallyById(Long id, Song songFromRequest) {
//        Song songFromDatabase = songRetriever.findById(id);
//        Song.SongBuilder songBuilder = Song.builder();
//
//        if (songFromRequest.getName() != null) {
//            songBuilder.name(songFromRequest.getName());
//        } else {
//            songBuilder.name(songFromDatabase.getName());
//        }
//        if (songFromRequest.getArtist() != null) {
//            songBuilder.artist(songFromRequest.getArtist());
//        } else {
//            songBuilder.artist(songFromDatabase.getArtist());
//        }
//        Song toSave = songBuilder.build();
//        updateById(id, toSave);
//        return toSave;
//    }

//    Dirty Checking version
//    (Update with help of Hibernate, but without using dedicated service which is SongRepository
//    , it is fast and nice, but wrong with programming convention! Use only in personal projects.)

//         void updateById(Long id, Song newSong) {
//        Song songById = songRetriever.findById(id);
//        songById.setName(newSong.getName());
//        songById.setArtist(newSong.getArtist());
//    }
//
//     Song updatePartiallyById(Long id, Song songFromRequest) {
//        Song songFromDatabase = songRetriever.findById(id);
//        if (songFromRequest.getName() != null) {
//            songFromDatabase.setName(songFromRequest.getName());
//        }
//        if (songFromRequest.getArtist() != null) {
//            songFromDatabase.setArtist(songFromRequest.getArtist());
//        }
//        return songFromDatabase;
//    }
}
