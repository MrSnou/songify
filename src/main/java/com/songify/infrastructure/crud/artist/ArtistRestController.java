package com.songify.infrastructure.crud.artist;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistResponseDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistUpdateRequestDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistUpdateResponseDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistWithAlbumsResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/artists")
class ArtistRestController {
    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping("/addArtist")
    ResponseEntity<ArtistDto> addArtist(@RequestBody ArtistRequestDto requestDto) {
        ArtistDto artistDto = songifyCrudFacade.addArtist(requestDto);
        return ResponseEntity.ok(artistDto);
    }

    @GetMapping
    ResponseEntity<AllArtistDto> findAllArtists(Pageable pageable) {
        Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists(pageable);
        return ResponseEntity.ok(new AllArtistDto(allArtists));
    }

    @DeleteMapping("/deleteArtist/{artistId}")
    ResponseEntity<ArtistResponseDto> deleteArtist(@PathVariable Long artistId) {
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);
        ArtistResponseDto responseDto = new ArtistResponseDto(HttpStatus.OK, "Artist with id " + artistId + " successfully deleted.");
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/addArtistToAlbum/{artistId}/{albumId}")
    ResponseEntity<ArtistResponseDto> addArtistToAlbum(@PathVariable Long artistId, @PathVariable Long albumId) {
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        ArtistResponseDto responseDto = new ArtistResponseDto(HttpStatus.OK, "Artist with id " + artistId
                + " successfully added to the album with id " +  albumId + " successfully added.");
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/changeArtistName/{artistId}")
    ResponseEntity<ArtistUpdateResponseDto> changeArtistName(@PathVariable final Long artistId,
                                                             @Valid @RequestBody ArtistUpdateRequestDto requestDto) {
        ArtistDto oldArtistDto = songifyCrudFacade.findArtistById(artistId);
        songifyCrudFacade.updateArtistNameById(artistId, requestDto.newArtistName());
        ArtistUpdateResponseDto responseDto = new ArtistUpdateResponseDto(HttpStatus.OK, "Successfully updated artist [id = "+ artistId +"] name from "
                + oldArtistDto.name() + " to " + requestDto.newArtistName());
        return  ResponseEntity.ok(responseDto);
    }

    @PostMapping("/addArtistWithDefaultAlbumAndSong")
    ResponseEntity<ArtistResponseDto> addArtistWithDefaultAlbumAndSong(@RequestBody ArtistRequestDto requestDto) {
        ArtistDto artistDto = songifyCrudFacade.addArtistWithDefaultAlbumAndSong(requestDto);
        ArtistResponseDto responseDto = new ArtistResponseDto(HttpStatus.CREATED,
                "Successfully created new artist ["+ artistDto.name() +"] and assigned default Album and Song to it.");
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/findArtistWithAlbums/{artistId}")
    ResponseEntity<ArtistWithAlbumsResponseDto> findArtistWithAlbums(@PathVariable Long artistId) {
        ArtistWithAlbumsResponseDto artistDtoWithAlbumsDto = songifyCrudFacade.findArtistDtoWithAlbumsDto(artistId);
        return ResponseEntity.ok(artistDtoWithAlbumsDto);
    }
}
