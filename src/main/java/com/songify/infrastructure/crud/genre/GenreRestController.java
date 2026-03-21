package com.songify.infrastructure.crud.genre;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.infrastructure.crud.genre.dto.request.GenreRequestDto;
import com.songify.infrastructure.crud.genre.dto.response.AllGenresResponseDto;
import com.songify.infrastructure.crud.genre.dto.response.GenreResponseDto;
import com.songify.infrastructure.crud.genre.dto.response.GenreWithSongsResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
class GenreRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping("/{genreId}")
    ResponseEntity<GenreDto> getGenreById(@PathVariable Long genreId) {
        GenreDto genreDtoById = songifyCrudFacade.findGenreDtoById(genreId);
        return ResponseEntity.ok(genreDtoById);
    }

    @GetMapping
    ResponseEntity<AllGenresResponseDto> getAllGenres(@PageableDefault(sort = "id") Pageable pageable) {
        List<GenreDto> all = songifyCrudFacade.findAllGenreDto(pageable);
        AllGenresResponseDto responseDto = new AllGenresResponseDto("Successfully retrieved all genres", all);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{genreId}/songs")
    ResponseEntity<GenreWithSongsResponseDto> getGenreWithSongs(@PathVariable Long genreId) {
        GenreWithSongsResponseDto responseDto = songifyCrudFacade.findGenreDtoWithSongsDto(genreId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    ResponseEntity<GenreDto> addGenre(@RequestBody GenreRequestDto requestDto) {
        GenreDto genreDto = songifyCrudFacade.addGenre(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(genreDto);
    }

    @DeleteMapping("/{genreId}")
    ResponseEntity<GenreResponseDto> deleteGenre(@PathVariable Long genreId) {
        songifyCrudFacade.deleteGenreById(genreId);
        GenreResponseDto responseDto = new GenreResponseDto(HttpStatus.OK, "Successfully Deleted Genre with id " + genreId + ".");
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{genreId}")
    ResponseEntity<GenreResponseDto> updateNameOfGenreById(@PathVariable Long genreId, @RequestBody GenreRequestDto requestDto) {
        songifyCrudFacade.updateGenreNameById(genreId, requestDto.name());
        GenreResponseDto responseDto = new GenreResponseDto(HttpStatus.OK, "Successfully changed Genre with id " + genreId + " to " + requestDto.name() + ".");
        return ResponseEntity.ok(responseDto);
    }
}
