package com.songify.infrastructure.crud.song;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.dto.request.UpdateGenreDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongAlbumRequestDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.CreateSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.GetAllSongsResponseDto;
import com.songify.infrastructure.crud.song.dto.response.SongWithGenreResponseDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongResponseDto;
import com.songify.infrastructure.crud.song.util.SongDto;
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
    ResponseEntity<GetAllSongsResponseDto> getAllSongs(@PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        GetAllSongsResponseDto response = GetAllSongsResponseDto.builder().songs(songifyCrudFacade.findAllSongs(pageable)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{songId}")
    ResponseEntity<SongDto> getSongById(@PathVariable Long songId) {
        SongDto responseDto = songifyCrudFacade.findSongDtoById(songId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{songId}/genres")
    ResponseEntity<SongWithGenreResponseDto> getSongWithGenreById(@PathVariable Long songId) {
        SongWithGenreResponseDto responseDto = songifyCrudFacade.findSongDtoWithGenreDtoById(songId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid SongRequestDto requestDto) {
        CreateSongResponseDto responseDto = new CreateSongResponseDto(songifyCrudFacade.addSong(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{songId}")
    ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Long songId) {
        DeleteSongResponseDto responseDto = songifyCrudFacade.deleteSongById(songId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{songId}")
    ResponseEntity<UpdateSongResponseDto> partiallyUpdateSong(@PathVariable Long songId, @RequestBody UpdateSongRequestDto request) {
        UpdateSongResponseDto updateSongResponseDto = songifyCrudFacade.updatesSongPartiallyById(songId, request);
        return ResponseEntity.ok(updateSongResponseDto);
    }

    @PatchMapping("/{songId}/albums")
    ResponseEntity<UpdateSongAlbumResponseDto> updateSongAlbum(@PathVariable Long songId, @RequestBody UpdateSongAlbumRequestDto request) {
        UpdateSongAlbumResponseDto responseDto = songifyCrudFacade.updateSongAlbum(songId, request);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{songId}/genres")
    ResponseEntity<UpdateSongResponseDto> updateSongGenre(@PathVariable Long songId, @RequestBody UpdateGenreDto requestDto) {
        UpdateSongResponseDto responseDto = songifyCrudFacade.updateSongGenreById(songId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
