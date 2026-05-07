package com.songify.infrastructure.crud.song;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.infrastructure.crud.song.dto.SongRequestDto;
import com.songify.infrastructure.crud.song.dto.UpdateSongRequestDto;
import com.songify.domain.crud.dto.song.CreateSongDto;
import com.songify.domain.crud.dto.song.DeleteSongResponseDto;
import com.songify.domain.crud.dto.song.AllSongsDto;
import com.songify.domain.crud.dto.song.UpdateSongAlbumResponseDto;
import com.songify.domain.crud.dto.song.UpdateSongResponseDto;
import com.songify.domain.crud.dto.song.SongDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
@RequestMapping("/songs")
@AllArgsConstructor
class SongRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping
    ResponseEntity<AllSongsDto> getAllSongs(@PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(songifyCrudFacade.getAllSongsDto(pageable));
    }

    @GetMapping("/{songId}")
    ResponseEntity<SongDto> getSongById(@PathVariable Long songId) {
        SongDto responseDto = songifyCrudFacade.findSongDtoById(songId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    ResponseEntity<CreateSongDto> postSong(@RequestBody @Valid SongRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songifyCrudFacade.addSong(requestDto));
    }

    @DeleteMapping("/{songId}")
    ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Long songId) {
        return ResponseEntity.ok(songifyCrudFacade.deleteSongById(songId));
    }

    @PatchMapping("/{songId}")
    ResponseEntity<UpdateSongResponseDto> partiallyUpdateSong(@PathVariable Long songId, @RequestBody UpdateSongRequestDto request) {
        UpdateSongResponseDto updateSongResponseDto = songifyCrudFacade.updatesSongPartiallyById(songId, request);
        return ResponseEntity.ok(updateSongResponseDto);
    }

    @PatchMapping("/{songId}/albums/{albumId}")
    ResponseEntity<UpdateSongAlbumResponseDto> updateSongAlbum(@PathVariable Long songId, @PathVariable Long albumId) {
        UpdateSongAlbumResponseDto responseDto = songifyCrudFacade.updateSongAlbum(songId, albumId);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{songId}/genres/{genreId}")
    ResponseEntity<UpdateSongResponseDto> updateSongGenre(@PathVariable Long songId, @PathVariable Long genreId) {
        UpdateSongResponseDto responseDto = songifyCrudFacade.updateSongGenreById(songId, genreId);
        return ResponseEntity.ok(responseDto);
    }
}
