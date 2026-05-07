package com.songify.domain.crud.dto.album;

import com.songify.domain.crud.dto.song.SongDto;

import java.util.List;

public record AlbumDto(Long id, String title, List<SongDto> songs) {
}
