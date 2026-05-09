package com.songify.domain.crud.dto.artist;

import lombok.Builder;

@Builder
public record ArtistDto(Long id, String name) {
}
