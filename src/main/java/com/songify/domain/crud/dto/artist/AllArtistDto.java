package com.songify.domain.crud.dto.artist;

import lombok.Builder;

import java.util.Set;

@Builder
public record AllArtistDto(Set<ArtistDto> allArtists) {
}
