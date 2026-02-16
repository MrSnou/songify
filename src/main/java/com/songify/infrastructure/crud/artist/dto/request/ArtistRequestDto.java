package com.songify.infrastructure.crud.artist.dto.request;

import lombok.Builder;

@Builder
public record ArtistRequestDto(String name) {
}
