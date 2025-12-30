package com.songify.song.domain.service;

import com.songify.song.domain.model.Song;
import com.songify.song.domain.repository.SongRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class SongUpdater {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;


    public void updateById(Long id, Song newSong) {
        songRetriever.existsById(id);
        log.info("Updating song with id: " + id);
        songRepository.updateById(id, newSong);
    }

    public Song updatePartiallyById(Long id, Song songFromRequest) {
        Song songFromDatabase = songRetriever.findById(id);
        Song.SongBuilder songBuilder = Song.builder();

        if (songFromRequest.getName() != null) {
            songBuilder.name(songFromRequest.getName());
        } else {
            songBuilder.name(songFromDatabase.getName());
        }
        if (songFromRequest.getArtist() != null) {
            songBuilder.artist(songFromRequest.getArtist());
        } else {
            songBuilder.artist(songFromDatabase.getArtist());
        }
        Song toSave = songBuilder.build();
        updateById(id, toSave);
        return toSave;
    }

//    Dirty Checking version
//    (Update with help of Hibernate, but without using dedicated service which is SongRepository
//    , it is fast and nice, but wrong with programming convention! Use only in personal projects.)

//        public void updateById(Long id, Song newSong) {
//        Song songById = songRetriever.findById(id);
//        songById.setName(newSong.getName());
//        songById.setArtist(newSong.getArtist());
//    }
//
//    public Song updatePartiallyById(Long id, Song songFromRequest) {
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
