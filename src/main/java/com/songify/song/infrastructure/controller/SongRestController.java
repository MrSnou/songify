package com.songify.song.infrastructure.controller;

import com.songify.song.domain.service.SongAdder;
import com.songify.song.domain.service.SongRetriever;
import com.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.domain.model.SongNotFoundException;
import com.songify.song.domain.model.Song;
import com.songify.song.infrastructure.controller.dto.response.*;
import com.songify.song.domain.service.SongDeleter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.songify.song.domain.service.SongMapper.*;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/songs")
public class SongRestController {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;
    private final SongDeleter songDeleter;


    @GetMapping
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        List<Song> allSongs = songRetriever.findAll();
        if (limit != null) {
            List<Song> limitedMap = songRetriever.findAllLimited(limit);
            GetAllSongsResponseDto response = new GetAllSongsResponseDto(limitedMap);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        GetAllSongsResponseDto response = mapFromSongToGetAllSongsResponseDto(allSongs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(@PathVariable(required = true) Long id) {
        log.info("Requested song with id {}", id);

        Song song = songRetriever.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));

        GetSongResponseDto response = mapFromSongToSongResponseDto(song);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        Song song = mapFromCreateSongRequestDtoToSong(request);
        songAdder.addSong(song);
        CreateSongResponseDto response = mapFromSongToCreateSongResponseDto(song);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByPathVariableId(@PathVariable(required = true) Long id) {
        songRetriever.existsById(id);
        songDeleter.deleteById(id);
        DeleteSongResponseDto response = DeleteSongResponseDto(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<DeleteSongResponseDto> deleteSongsByQueryParamId(@RequestParam(required = true) Long id) {
        songRetriever.existsById(id);
        songDeleter.deleteById(id);
        DeleteSongResponseDto response = DeleteSongResponseDto(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> update(@RequestBody @Valid UpdateSongRequestDto request, @PathVariable(required = true) Integer id) {
        List<Song> allSongs = songRetriever.findAll();
        if (!allSongs.contains(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }
        Song newSong = mapFromUpdateSongResponseDtoToSong(request);
        Song oldSong = songAdder.addSong(newSong);
        log.info("Updated song with id: " + id +
                " | new title: " + newSong.getName() + " from " + oldSong.getName() +
                " and artist to: " + newSong.getArtist() + " from " + oldSong.getArtist());
        UpdateSongResponseDto response = MapFromSongToUpdateSongResponseDto(newSong);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSong(@RequestBody PartiallyUpdateSongRequestDto request,
                                                                              @PathVariable(required = true) Integer id) {
        List<Song> allSongs = songRetriever.findAll();
        if (!allSongs.contains(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }

        Song songFromDatabase = allSongs.get(id);
        Song updatedSong = mapFromPartiallyUpdateSongRequestDtoToSong(request);
        Song.SongBuilder songBuilder = Song.builder();
        if (updatedSong.getName() != null) {
            songBuilder.name(updatedSong.getName());
        } else {
            songBuilder.name(songFromDatabase.getName());
        }
        if (updatedSong.getArtist() != null) {
            songBuilder.artist(updatedSong.getArtist());
        } else {
            songBuilder.artist(songFromDatabase.getArtist());
        }
        songAdder.addSong(updatedSong);

        log.info("Partially updated songFromDatabase with id: " + id +
                " | new title: " + updatedSong.getName() + " from " + songFromDatabase.getName() +
                " and artist to: " + updatedSong.getArtist() + " from " + songFromDatabase.getArtist());

        PartiallyUpdateSongResponseDto response = mapFromSongToPartiallyUpdateSongResponseDto(updatedSong);
        return ResponseEntity.ok(response);


    }


}
