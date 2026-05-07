package com.songify.infrastructure.crud.album;


import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.album.dto.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.domain.crud.dto.album.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.domain.crud.dto.album.AlbumResponseDto;
import com.songify.domain.crud.dto.album.AllAlbumsResponseDto;
import com.songify.domain.crud.dto.album.DeleteAlbumResponseDto;
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

@RestController
@AllArgsConstructor
@RequestMapping("/albums")
class AlbumRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping()
    ResponseEntity<AllAlbumsResponseDto> getAllAlbums(@PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(songifyCrudFacade.findAllAlbumDto(pageable));
    }

    @GetMapping("/{requestAlbumId}")
    ResponseEntity<AlbumDtoWithArtistsAndSongsResponseDto> getAlbumWithArtistsAndSongsById(@PathVariable Long requestAlbumId) {
        return ResponseEntity.ok(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(requestAlbumId));
    }

    @PostMapping
    ResponseEntity<AlbumDto> postAlbum(@RequestBody AlbumWithSongRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(songifyCrudFacade.addAlbumWithSong(requestDto));
    }

    @DeleteMapping("/{requestAlbumId}")
    ResponseEntity<DeleteAlbumResponseDto> deleteAlbumById(@PathVariable Long requestAlbumId) {
        return ResponseEntity.ok(songifyCrudFacade.deleteAlbumById(requestAlbumId));

    }

    @PatchMapping("/{albumId}")
    ResponseEntity<AlbumResponseDto> updateAlbumWithSongsAndArtists
            (@PathVariable Long albumId, @RequestBody UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {
        return ResponseEntity.ok(songifyCrudFacade.updateAlbumByIdWithSongsAndArtists(albumId, requestDto));
    }
}
