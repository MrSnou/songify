package com.songify.infrastructure.crud.song;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.genre.dto.request.UpdateGenreDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongAlbumRequestDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.CreateSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.GetAllSongsResponseDto;
import com.songify.infrastructure.crud.song.dto.response.GetSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongResponseDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.songify.infrastructure.crud.song.util.SongControllerMapper.mapFromSongToCreateSongResponseDto;
import static com.songify.infrastructure.crud.song.util.SongControllerMapper.mapFromSongToDeleteSongResponseDto;
import static com.songify.infrastructure.crud.song.util.SongControllerMapper.mapFromSongToGetAllSongsResponseDto;


@RestController
@Log4j2
@RequestMapping("/api/v1/songs")
@AllArgsConstructor
public
class SongRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping
    ResponseEntity<GetAllSongsResponseDto> getAllSongs(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        List<SongDto> allSongs = songifyCrudFacade.findAllSongs(pageable);
        GetAllSongsResponseDto response = mapFromSongToGetAllSongsResponseDto(allSongs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<GetSongResponseDto> getSongById(@PathVariable Long id) {
        SongDto song = songifyCrudFacade.findSongDtoById(id);
        SongGenreDto genre = songifyCrudFacade.findSongGenreDtoById(id);
        GenreDto genreDto = new GenreDto(genre.genre().id(),  genre.genre().name());

        GetSongResponseDto responseDto = new GetSongResponseDto(song, genreDto);
        return ResponseEntity.ok(responseDto);
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

    @PatchMapping("/updateSongNameAndDuration/{songId}")
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

    @PatchMapping("/updateSongAlbum/{songId}")
    ResponseEntity<UpdateSongAlbumResponseDto> updateSongAlbum(@PathVariable Long songId, @RequestBody UpdateSongAlbumRequestDto request) {
        UpdateSongAlbumResponseDto responseDto = songifyCrudFacade.updateSongAlbum(songId, request);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/updateSongGenre/{songId}")
    ResponseEntity<UpdateSongResponseDto> updateSongGenre(@PathVariable Long songId, @RequestBody UpdateGenreDto requestDto) {
        SongGenreDto oldSongGenre = songifyCrudFacade.findSongGenreDtoById(songId);
        GenreDto newSongGenre = songifyCrudFacade.findGenreDtoById(requestDto.genreId());
        SongDto updatedSong = songifyCrudFacade.findSongDtoById(songId);

        songifyCrudFacade.updateSongGenreById(songId, requestDto.genreId());

        UpdateSongResponseDto responseDto = new UpdateSongResponseDto(
                "Successfully updated song genre from: " + oldSongGenre.genre().name() +
                        " to " + newSongGenre.name() + ".",
                updatedSong
                );

        return ResponseEntity.ok(responseDto);
    }
}
