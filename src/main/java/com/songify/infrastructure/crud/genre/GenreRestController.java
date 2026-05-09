package com.songify.infrastructure.crud.genre;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.genre.GenreDto;
import com.songify.domain.crud.dto.genre.GenreRequestDto;
import com.songify.domain.crud.dto.genre.AllGenresDto;
import com.songify.domain.crud.dto.genre.GenreResponseDto;
import com.songify.domain.crud.dto.genre.GenreWithSongsResponseDto;
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
@RequestMapping("/genres")
class GenreRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping("/{genreId}")
    ResponseEntity<GenreDto> getGenreById(@PathVariable Long genreId) {
        return ResponseEntity.ok(songifyCrudFacade.findGenreDtoById(genreId));
    }

    @GetMapping
    ResponseEntity<AllGenresDto> getAllGenres(@PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(songifyCrudFacade.findAllGenreDto(pageable));
    }

    @GetMapping("/{genreId}/songs")
    ResponseEntity<GenreWithSongsResponseDto> getGenreWithSongs(@PathVariable Long genreId) {
        GenreWithSongsResponseDto responseDto = songifyCrudFacade.findGenreDtoWithSongsDto(genreId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    ResponseEntity<GenreDto> addGenre(@Valid @RequestBody GenreRequestDto requestDto) {
        GenreDto genreDto = songifyCrudFacade.addGenre(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(genreDto);
    }

    @DeleteMapping("/{genreId}")
    ResponseEntity<?> deleteGenre(@PathVariable Long genreId) {
        songifyCrudFacade.deleteGenreById(genreId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{genreId}")
    ResponseEntity<GenreResponseDto> updateNameOfGenreById(@PathVariable Long genreId,@Valid @RequestBody GenreRequestDto requestDto) {
        GenreResponseDto genreResponseDto = songifyCrudFacade.updateGenreNameById(genreId, requestDto.name());
        return ResponseEntity.ok(genreResponseDto);
    }
}
