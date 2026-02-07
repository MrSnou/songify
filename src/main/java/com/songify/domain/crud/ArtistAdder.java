package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongLanguageDto;
import com.songify.domain.crud.dto.SongRequestDto;
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

    ArtistDto addArtistWithDefaultAlbumAndSong(final ArtistRequestDto requestDto) {
        Artist save = saveArtistWithDefaultAlbumAndSong(requestDto.name());
        return new ArtistDto(save.getId(), save.getName());
    }
    private Artist saveArtistWithDefaultAlbumAndSong(final String name) {
        Album album = albumAdder.addAlbum(
                "default_album_" + UUID.randomUUID(),
                LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC));
        Song song = songAdder.addSong(new SongRequestDto(
                "default-song_" + UUID.randomUUID(),
                100L,
                LocalDateTime.now().toInstant(ZoneOffset.UTC),
                SongLanguageDto.OTHER
        ));
        Artist artist = new Artist(name);

        album.addSongToAlbum(song);
        artist.addAlbum(album);

        return artistRepository.save(artist);
    }

    private Artist saveArtist(final String name) {
        Artist artist = new Artist(name);
        Artist save = artistRepository.save(artist);
        return save;
    }
}
