package com.songify.domain.crud;

import com.songify.domain.crud.exceptions.AlbumNotFoundException;
import com.songify.domain.crud.dto.AlbumDtoWithArtistsAndSongs;
import com.songify.domain.crud.dto.ArtistInfoDto;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.SongInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class AlbumRetriever {

    private final AlbumRepository albumRepository;


    AlbumDtoWithArtistsAndSongs findAlbumByIdWithArtistsAndSongs(final Long id) {

        Album albumByIdFromDb = albumRepository.findAlbumByIdWithSongsAndArtists(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));

        Set<Artist> artists = albumByIdFromDb.getArtists();
        Set<Song> songs = albumByIdFromDb.getSongs();

        Set<ArtistInfoDto> ArtistDtos = artists.stream()
                .map( artist -> new ArtistInfoDto(artist.getId(), artist.getName()))
                .collect(Collectors.toSet());

        Set<SongInfoDto> SongDtos = songs.stream()
                .map(song -> new SongInfoDto(song.getId(), song.getName(), song.getDuration(),
                        song.getReleaseDate(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .collect(Collectors.toSet());

        return new AlbumDtoWithArtistsAndSongs(
                albumByIdFromDb.getId(),
                albumByIdFromDb.getTitle(),
                albumByIdFromDb.getReleaseDate(),
                ArtistDtos,
                SongDtos
                );
    }

    Album findAlbumById(final Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found in db."));
    }
}
