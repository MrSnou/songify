package com.songify.infrastructure.crud.album.dto.response;

import com.songify.domain.crud.dto.AlbumDto;

import java.util.List;

public record AllAlbumsResponseDto(List<AlbumDto> allAlbums) {
}
