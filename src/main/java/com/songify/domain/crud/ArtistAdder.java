package com.songify.domain.crud;

import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.song.util.SongLanguageDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@AllArgsConstructor
class ArtistAdder {

    private final ArtistRepository artistRepository;
    private final AlbumAdder albumAdder;
    private final SongAdder songAdder;

    ArtistDto addArtist(final String name) {
        Artist artist = new Artist(name);
        Artist save = artistRepository.save(artist);
        return new ArtistDto(save.getId(), save.getName());
    }

    private Artist saveArtist(final String name) {
        Artist artist = new Artist(name);
        Artist save = artistRepository.save(artist);
        return save;
    }
}
