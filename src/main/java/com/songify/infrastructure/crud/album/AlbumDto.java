package com.songify.infrastructure.crud.album;

import com.songify.infrastructure.crud.song.util.SongDto;

import java.util.List;

public record AlbumDto(Long id, String title, List<SongDto> songs) {
}
