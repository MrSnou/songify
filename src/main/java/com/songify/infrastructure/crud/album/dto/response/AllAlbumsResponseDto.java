package com.songify.infrastructure.crud.album.dto.response;

import com.songify.infrastructure.crud.album.AlbumDto;

import java.util.List;

public record AllAlbumsResponseDto(List<AlbumDto> allAlbums) {
}
