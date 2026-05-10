package com.songify.infrastructure.crud.artist;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.artist.AllArtistDto;
import com.songify.domain.crud.dto.artist.ArtistDto;
import com.songify.domain.crud.dto.artist.ArtistRequestDto;
import com.songify.domain.crud.dto.artist.ArtistUpdateRequestDto;
import com.songify.domain.crud.dto.artist.UpdateArtistAlbumResponseDto;
import com.songify.domain.crud.dto.artist.ArtistUpdateResponseDto;
import com.songify.domain.crud.dto.artist.ArtistWithAlbumsResponseDto;
import jakarta.validation.Valid;
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
@RequestMapping("/artists")
class ArtistRestController {
    private final SongifyCrudFacade songifyCrudFacade;


    @GetMapping("/{artistId}")
    ResponseEntity<ArtistDto> getArtistById(@PathVariable Long artistId) {
        return ResponseEntity.ok(songifyCrudFacade.findArtistById(artistId));
    }

    @GetMapping
    ResponseEntity<AllArtistDto> getAllArtists(@PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(songifyCrudFacade.findAllArtists(pageable));
    }

    @GetMapping("/{artistId}/albums")
    ResponseEntity<ArtistWithAlbumsResponseDto> getArtistWithAlbums(@PathVariable Long artistId) {
        ArtistWithAlbumsResponseDto artistDtoWithAlbumsDto = songifyCrudFacade.findArtistDtoWithAlbumsDto(artistId);
        return ResponseEntity.ok(artistDtoWithAlbumsDto);
    }

    @PostMapping()
    ResponseEntity<ArtistDto> postArtist(@Valid @RequestBody ArtistRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songifyCrudFacade.addArtist(requestDto));
    }

    @PatchMapping("/{artistId}/albums/{albumId}")
    ResponseEntity<UpdateArtistAlbumResponseDto> updateArtistToAlbum(@PathVariable Long artistId, @PathVariable Long albumId) {
        UpdateArtistAlbumResponseDto updateArtistAlbumResponseDto = songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        return ResponseEntity.ok(updateArtistAlbumResponseDto);
    }

    @PatchMapping("/{artistId}")
    ResponseEntity<ArtistUpdateResponseDto> updateArtistName(@PathVariable final Long artistId,
                                                             @Valid @RequestBody ArtistUpdateRequestDto requestDto) {
        ArtistUpdateResponseDto artistUpdateResponseDto = songifyCrudFacade.updateArtistNameById(artistId, requestDto.newArtistName());
        return ResponseEntity.ok(artistUpdateResponseDto);
    }

    @DeleteMapping("/{artistId}")
    ResponseEntity<?> deleteArtist(@PathVariable Long artistId) {
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
