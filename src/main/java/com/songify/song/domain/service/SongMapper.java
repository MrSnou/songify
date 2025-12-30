package com.songify.song.domain.service;

import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.response.*;
import com.songify.song.domain.model.Song;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

public class SongMapper {
    public static Song mapFromCreateSongRequestDtoToSong(CreateSongRequestDto dto) {
        return new Song(dto.songName(), dto.artist());
    }

    public static CreateSongResponseDto mapFromSongToCreateSongResponseDto(Song song) {
        SongDto songDto = mapFromSongToSongDto(song);
        return new CreateSongResponseDto(songDto);

    }
    public static SongDto mapFromSongToSongDto(Song song) {
        return new SongDto(song.getId(), song.getName(), song.getArtist());
    }

    public static GetAllSongsResponseDto mapFromSongToGetAllSongsResponseDto(List<Song> songs) {
        List<SongDto> songDtos = songs.stream()
                .map(SongMapper::mapFromSongToSongDto).toList();
        return new GetAllSongsResponseDto(songDtos);
    }

    public static GetSongResponseDto mapFromSongToSongResponseDto(Song song) {
        SongDto songDto = mapFromSongToSongDto(song);
        return new GetSongResponseDto(songDto);
    }


    public static DeleteSongResponseDto DeleteSongResponseDto(Long id) {
        return new DeleteSongResponseDto("You deleted song with id: " + id, HttpStatus.OK);
    }

    public static UpdateSongResponseDto MapFromSongToUpdateSongResponseDto(Song newSong) {
        return new UpdateSongResponseDto(newSong.getName(), newSong.getArtist());
    }

    public static Song mapFromUpdateSongResponseDtoToSong(@Valid UpdateSongRequestDto request) {
        return new Song(request.songName(), request.artist());
    }

    public static Song mapFromPartiallyUpdateSongRequestDtoToSong(PartiallyUpdateSongRequestDto request) {
        return new Song(request.songName(), request.artist());
    }

    public static PartiallyUpdateSongResponseDto mapFromSongToPartiallyUpdateSongResponseDto(Song song) {
        SongDto songDto = mapFromSongToSongDto(song);
        return new PartiallyUpdateSongResponseDto(songDto);
    }
}
