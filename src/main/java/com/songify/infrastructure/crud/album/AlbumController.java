package com.songify.infrastructure.crud.album;


import com.songify.domain.crud.dto.AlbumDtoWithArtistsAndSongs;
import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumRequestDto;
import com.songify.domain.crud.dto.AlbumResponseDto;
import com.songify.domain.crud.dto.DeleteAlbumResponseDto;
import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsDto;
import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/albums")
class AlbumController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<AlbumDto> postAlbum(@RequestBody AlbumRequestDto requestDto) {
        AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(requestDto);
        return ResponseEntity.ok(albumDto);
    }

    @GetMapping("/getAlbumById/{requestAlbumId}")
    ResponseEntity<AlbumDtoWithArtistsAndSongs> getAlbumWithArtistsAndSongsById(@PathVariable Long requestAlbumId) {
        AlbumDtoWithArtistsAndSongs responseDto = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(requestAlbumId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/deleteAlbumById/{requestAlbumId}")
    ResponseEntity<DeleteAlbumResponseDto> deleteAlbumById(@PathVariable Long requestAlbumId) {
        songifyCrudFacade.deleteAlbumById(requestAlbumId);
        DeleteAlbumResponseDto response = new DeleteAlbumResponseDto(HttpStatus.OK, "Deleting genre with id " + requestAlbumId + " successfully deleted.");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/updateAlbum/{albumId}")
    ResponseEntity<AlbumResponseDto>
    updateAlbumWithSongsAndArtists(@PathVariable Long albumId,
                                   @RequestBody UpdateAlbumWithSongsAndArtistsDto requestDto) {
        UpdateAlbumWithSongsAndArtistsResponseDto savedAlbum = songifyCrudFacade.updateAlbumByIdWithSongsAndArtists(albumId, requestDto);
        AlbumResponseDto responseDto = new AlbumResponseDto(HttpStatus.OK, "Successfully updated album", savedAlbum);
        return ResponseEntity.ok(responseDto);
    }

}
