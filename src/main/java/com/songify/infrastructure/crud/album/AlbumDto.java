package com.songify.infrastructure.crud.album;

import java.util.List;

public record AlbumDto(Long id, String title, List<Long> songIds) {
}
