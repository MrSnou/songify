package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongInfoDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.infrastructure.crud.album.error.AlbumNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
@AllArgsConstructor
class AlbumRetriever {

    private final AlbumRepository albumRepository;


    List<Album> findAllAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable).getContent();
    }


    AlbumDtoWithArtistsAndSongsResponseDto findAlbumByIdWithArtistsAndSongs(final Long id) {

        Album albumByIdFromDb = albumRepository.findAlbumByIdWithSongsAndArtists(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));

        Set<Artist> artists = albumByIdFromDb.getArtists();
        Set<Song> songs = albumByIdFromDb.getSongs();

        Set<ArtistDto> ArtistDtos = artists.stream()
                .map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                .collect(Collectors.toSet());

        Set<SongInfoDto> SongDtos = songs.stream()
                .map(song -> new SongInfoDto(song.getId(), song.getName(), song.getDuration(),
                        song.getReleaseDate(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .collect(Collectors.toSet());

        return new AlbumDtoWithArtistsAndSongsResponseDto(
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
                    map(song -> new SongDto(song.getId(), song.getName(), song.getDuration()))
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

    List<AlbumDto> findAllAlbumsDto(Pageable pageable) {
        List<Album> all = findAllAlbums(pageable);
        List<AlbumDto> allAlbumsDto = all.stream()
                .map(album -> new AlbumDto(album.getId(), album.getTitle()))
                .collect(Collectors.toList());
        return allAlbumsDto;
    }
}
