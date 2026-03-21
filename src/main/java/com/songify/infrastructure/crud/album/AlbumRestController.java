package com.songify.infrastructure.crud.album;


import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.infrastructure.crud.album.dto.request.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.album.dto.request.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumResponseDto;
import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.album.dto.response.DeleteAlbumResponseDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import lombok.AllArgsConstructor;
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

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/albums")
class AlbumRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping()
    ResponseEntity<AllAlbumsResponseDto> getAllAlbums(@PageableDefault(sort = "id") Pageable pageable) {
        List<AlbumDto> allAlbums = songifyCrudFacade.findAllAlbumDto(pageable);
        return ResponseEntity.ok(new AllAlbumsResponseDto(allAlbums));
    }

    @GetMapping("/{requestAlbumId}")
    ResponseEntity<AlbumDtoWithArtistsAndSongsResponseDto> getAlbumWithArtistsAndSongsById(@PathVariable Long requestAlbumId) {
        AlbumDtoWithArtistsAndSongsResponseDto responseDto = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(requestAlbumId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    ResponseEntity<AlbumDto> postAlbum(@RequestBody AlbumWithSongRequestDto requestDto) {
        AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(albumDto);
    }

    @DeleteMapping("/{requestAlbumId}")
    ResponseEntity<DeleteAlbumResponseDto> deleteAlbumById(@PathVariable Long requestAlbumId) {
        songifyCrudFacade.deleteAlbumById(requestAlbumId);
        DeleteAlbumResponseDto response = new DeleteAlbumResponseDto(HttpStatus.OK,
                "Album with id " + requestAlbumId + " successfully deleted.");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{albumId}")
    ResponseEntity<AlbumResponseDto> updateAlbumWithSongsAndArtists
            (@PathVariable Long albumId, @RequestBody UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {
        UpdateAlbumWithSongsAndArtistsResponseDto savedAlbum = songifyCrudFacade.updateAlbumByIdWithSongsAndArtists(albumId, requestDto);
        AlbumResponseDto responseDto = new AlbumResponseDto(HttpStatus.OK, "Successfully updated album.", savedAlbum);
        return ResponseEntity.ok(responseDto);
    }
}
