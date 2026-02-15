package com.songify.infrastructure.crud.artist;

import java.util.Set;

public record AllArtistDto(Set<ArtistDto> allArtists) {
}
