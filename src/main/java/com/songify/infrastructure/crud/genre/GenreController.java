package com.songify.infrastructure.crud.genre;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.GenreRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/genres")
class GenreController {
    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping("/addGenre")
    public ResponseEntity<GenreDto> addGenre(@RequestBody GenreRequestDto requestDto) {
        GenreDto genreDto = songifyCrudFacade.addGenre(requestDto);
        return ResponseEntity.ok(genreDto);
    }

    @DeleteMapping("/deleteGenre/{genreId}")
    public ResponseEntity<GenreResponseDto> deleteGenre(@PathVariable Long genreId) {
        songifyCrudFacade.deleteGenreById(genreId);
        GenreResponseDto responseDto = new GenreResponseDto(HttpStatus.OK, "Succesfully Deleted Genre with id " + genreId + ".");
        return ResponseEntity.ok(responseDto);
    }
}
