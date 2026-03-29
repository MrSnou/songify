package com.songify.domain.crud;

import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongResponseDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
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
    private final GenreRetriever genreRetriever;


    UpdateSongResponseDto updateById(Long songId, UpdateSongRequestDto songFromRequest) {
        Song oldSong = songRetriever.findSongById(songId);

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

        Song updatedSong = new Song(name, oldSong.getReleaseDate(), duration, oldSong.getLanguage(), oldSong.getGenre());
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
        return albumUpdater.addSongToAlbum(songId, albumId);
    }

    UpdateSongResponseDto updateSongGenreById(final Long songId, final Long genreId) {
        songRetriever.checkIfExists(songId);

        Genre newGenre = genreRetriever.findGenreById(genreId);

        songRepository.updateSongGenreById(songId, newGenre);
        SongDto updatedSongDto = songRetriever.findSongDtoById(songId);

        UpdateSongResponseDto response = UpdateSongResponseDto.builder()
                .message("Successfully updated song genre with id: " + songId + " to " +  newGenre.getName() + ".")
                .updatedSong(updatedSongDto)
                .build();

        return response;
    }

    void setGenreById(final Long songId, final Long genreId) {
        Song song = songRetriever.findSongById(songId);
        Genre genre = genreRetriever.findGenreById(genreId);
        song.setGenre(genre);
        songRepository.save(song);
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
