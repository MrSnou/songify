package com.songify.infrastructure.crud.song.controller;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.response.CreateSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.GetAllSongsResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.GetSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.UpdateSongResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.songify.infrastructure.crud.song.controller.SongControllerMapper.mapFromSongToCreateSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongControllerMapper.mapFromSongToDeleteSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongControllerMapper.mapFromSongToGetAllSongsResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongControllerMapper.mapFromSongToGetSongResponseDto;


@RestController
@Log4j2
@RequestMapping("/api/v1/songs")
@AllArgsConstructor
public
class SongRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping
    ResponseEntity<GetAllSongsResponseDto> getAllSongs(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        List<SongDto> allSongs = songifyCrudFacade.findAllSongs(pageable);
        GetAllSongsResponseDto response = mapFromSongToGetAllSongsResponseDto(allSongs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<GetSongResponseDto> getSongById(@PathVariable Long id, @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        SongDto song = songifyCrudFacade.findSongDtoById(id);
        GetSongResponseDto response = mapFromSongToGetSongResponseDto(song);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid SongRequestDto requestDto) {
        SongDto savedSong = songifyCrudFacade.addSong(requestDto);
        CreateSongResponseDto body = mapFromSongToCreateSongResponseDto(savedSong);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/deleteSong/{id}")
    ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Long id) {
        songifyCrudFacade.deleteSongById(id);
        DeleteSongResponseDto body = mapFromSongToDeleteSongResponseDto(id);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{songId}")
    ResponseEntity<UpdateSongResponseDto> partiallyUpdateSong(@PathVariable Long songId,
                                                              @RequestBody UpdateSongRequestDto request) {
        SongDto oldSongDto = songifyCrudFacade.updatesSongPartiallyById(songId, request);
        UpdateSongResponseDto response = new UpdateSongResponseDto(
                "Succesfully updated song with id: " + songId +
                        " song name from " + oldSongDto.name() + " to " + request.songName() +
                        " and duration from " + oldSongDto.duration() + " to " + request.duration() + "."
                , new SongDto(oldSongDto.id(), request.songName(), request.duration())
        );
        return ResponseEntity.ok(response);
    }
}
