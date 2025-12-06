package com.songify.song.controller;

import com.songify.song.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.dto.request.CreateSongRequestDto;
import com.songify.song.dto.request.UpdateSongRequestDto;
import com.songify.song.dto.response.*;
import com.songify.song.error.SongNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequestMapping("/songs")
public class SongRestController {

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Song 1", "Shawn Mendes"),
            2, new Song("Song 2", "Ariana Grande"),
            3, new Song("Song 3", "ACDC"),
            4, new Song("Song 4", "u2")));

    @GetMapping
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, Song> limitedMap = database
                    .entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new ResponseEntity<>(new GetAllSongsResponseDto(limitedMap), HttpStatus.OK);
        }

        GetAllSongsResponseDto response = new GetAllSongsResponseDto(database);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(@PathVariable(required = true) Integer id, @RequestHeader(required = false) String requestId) {
        log.info("Requested song with id {}", id);
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }
        GetSongResponseDto response = new GetSongResponseDto(database.get(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        Song song = new Song(request.songName(), request.artist());
        log.info("Added new song: {}", song);
        database.put(database.size() + 1, song);
        return new ResponseEntity<>(new CreateSongResponseDto(song), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByPathVariableId(@PathVariable(required = true) Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }
        Song songName = database.get(id);
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("You deleted song with id: " + id + " - \"" + songName + "\"", HttpStatus.OK));
    }

    @DeleteMapping
    public ResponseEntity<DeleteSongResponseDto> deleteSongsByQueryParamId(@RequestParam(required = true) Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }

        Song song = database.get(id);
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("You deleted song with id: " + id + " " + song.artist() + "-" + song.name(), HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> update(@RequestBody @Valid UpdateSongRequestDto request, @PathVariable(required = true) Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }
        String songNameToUpdate = request.songName();
        String newArtistToUpdate = request.artist();
        Song newSong = new Song(songNameToUpdate, newArtistToUpdate);
        Song oldSong = database.put(id, newSong);
        log.info("Updated song with id: " + id +
                " | new title: " + newSong.name() + " from " + oldSong.name() +
                " and artist to: " + newSong.artist() + " from " + oldSong.artist());
        return new ResponseEntity<>(new UpdateSongResponseDto(newSong.name(), newSong.artist()), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSong(@RequestBody PartiallyUpdateSongRequestDto request,
                                                                              @PathVariable(required = true) Integer id) {
        request.artist();
        request.songName();

        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " was not found in database!");
        }
        Song song = database.get(id);
        Song.SongBuilder songBuilder = Song.builder();
        if (request.songName() != null) {
            songBuilder.name(request.songName());
        } else {
            songBuilder.name(song.name());
        }
        if (request.artist() != null) {
            songBuilder.artist(request.artist());
        } else {
            songBuilder.artist(song.artist());
        }
        Song updatedSong = songBuilder.build();
        database.put(id, updatedSong);

        log.info("Partially updated song with id: " + id +
                " | new title: " + updatedSong.name() + " from " + song.name() +
                " and artist to: " + updatedSong.artist() + " from " + song.artist());

        return ResponseEntity.ok(new PartiallyUpdateSongResponseDto(updatedSong));


    }


}
