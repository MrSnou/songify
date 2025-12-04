package com.songify.song;

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
public class SongRestController {

    Map<Integer, String> database = new HashMap<>(Map.of(1, "Shawn Mendes song", 2, "Ariana Grande song", 3, "ACDC song", 4, "u2 song"));

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, String> limitedMap = database
                    .entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new ResponseEntity<>(new SongResponseDto(limitedMap), HttpStatus.OK);
        }

        SongResponseDto response = new SongResponseDto(database);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable(required = true) Integer id, @RequestHeader(required = false) String requestId) {
        String song = database.get(id);
        if (song == null) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        SingleSongResponseDto response = new SingleSongResponseDto(song);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> postSong(@RequestBody @Valid SongRequestDto request) {
        String songName = request.songName();
        log.info("Added new song: {}", songName);
        database.put(database.size() + 1, songName);
        return new ResponseEntity<>(new SingleSongResponseDto(songName), HttpStatus.OK);
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByPathVariableId(@PathVariable(required = true) Integer id) {
        if (!database.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DeleteSongResponseDto("No song with id " + id + " was found in database!", HttpStatus.NOT_FOUND));
        }
        String songName = database.get(id);
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("You deleted song with id: " + id + " - \"" + songName + "\"", HttpStatus.OK));
    }

    @DeleteMapping("/songs/")
    public ResponseEntity<DeleteSongResponseDto> deleteSongsByQueryParamId(@RequestParam(required = true) Integer id) {
        if (!database.containsKey(id)) {
            throw new RuntimeException("No song with id " + id + " was found in database!");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ErrorDeleteSongResponseDto("No song with id " + id + " was found in database!", HttpStatus.NOT_FOUND));
        }
        String songName = database.get(id);
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("You deleted song with id: " + id + " - \"" + songName + "\"", HttpStatus.OK));
    }

}
