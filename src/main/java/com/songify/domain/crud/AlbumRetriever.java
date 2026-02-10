package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDtoWithArtistsAndSongs;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistInfoDto;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongInfoDto;
import com.songify.domain.crud.dto.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.domain.crud.exceptions.AlbumNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

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
                .map(artist -> new ArtistInfoDto(artist.getId(), artist.getName()))
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

    UpdateAlbumWithSongsAndArtistsResponseDto getUpdateAlbumByIdWithSongsAndArtistsResponse(final Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found in db."));

        Set<ArtistDto> artists = null;
        Set<SongDto> songs = null;

        try {
            sleep(1000L);
            artists = album.getArtists()
                    .stream()
                    .map(artist ->
                            new ArtistDto(artist.getId(), artist.getName()))
                    .collect(Collectors.toSet());


            songs = album.getSongs().
                    stream().
                    map(song -> new SongDto(song.getId(), song.getName()))
                    .collect(Collectors.toSet());

        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            new RuntimeException(e.getMessage());
        }


        UpdateAlbumWithSongsAndArtistsResponseDto responseDto = new UpdateAlbumWithSongsAndArtistsResponseDto(
                album.getTitle(), artists, songs);

        return responseDto;
    }
}
