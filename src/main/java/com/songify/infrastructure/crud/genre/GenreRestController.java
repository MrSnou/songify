package com.songify.infrastructure.crud.genre;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.GenreRequestDto;
import com.songify.infrastructure.crud.genre.dto.response.AllGenresResponseDto;
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
@RequestMapping("api/v1/genres")
class GenreRestController {
    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping("/getAllGenres/")
    public ResponseEntity<AllGenresResponseDto> getAllGenres(@PageableDefault(sort = "id") Pageable pageable) {
        List<GenreDto> all = songifyCrudFacade.findAll(pageable);
        AllGenresResponseDto responseDto = new AllGenresResponseDto("Successfully retrieved all genres", all);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

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

    @PatchMapping("/updateNameOfGenre/{genreId}")
    public ResponseEntity<GenreResponseDto> changeNameOfGenreById(@PathVariable Long genreId, @RequestBody GenreRequestDto requestDto) {
        songifyCrudFacade.updateGenreNameById(genreId, requestDto.name());
        GenreResponseDto responseDto = new GenreResponseDto(HttpStatus.OK, "Successfully Changed Genre with id " + genreId + " to " + requestDto.name() + ".");
        return  ResponseEntity.ok(responseDto);
    }
}
