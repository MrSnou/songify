package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.request.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.domain.crud.exception.AlbumNotEmptyException;
import com.songify.domain.crud.exception.AlbumNotFoundException;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.domain.crud.exception.ArtistNotFoundException;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import com.songify.domain.crud.exception.SongNotFoundException;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class AlbumService {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    private final ArtistService artistService;

    AlbumDto addAlbumWithSong(final List<Long> songIds, final String title, final Instant releaseDate) {
        List<Song> songs = songIds.stream()
                .map(this::getSongFromDB)
                .toList();


        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        songs.forEach(album::addSongToAlbum);
        Album savedAlbum = albumRepository.save(album);
        return new AlbumDto(savedAlbum.getId(), savedAlbum.getTitle(),
                savedAlbum.getSongs()
                        .stream().map(
                                song -> new SongDto(song.getId(), song.getName(), song.getDuration(),
                                        new GenreDto(song.getGenre().getId(), song.getGenre().getName()))
                        ).toList());

    }

    void deleteById(Album album) {
        if (album.getSongs().isEmpty() && album.getArtists().isEmpty()) {
            albumRepository.deleteById(album.getId().longValue());
        } else {
            throw new AlbumNotEmptyException("Album is not empty, cannot delete");
        }
    }

    Set<Album> findAllAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable).stream().collect(Collectors.toSet());
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
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found"));
    }

    UpdateAlbumWithSongsAndArtistsResponseDto getUpdateAlbumByIdWithSongsAndArtistsResponse(final Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));

        Set<ArtistDto> artists = new HashSet<>();
        Set<SongDto> songs = new HashSet<>();


        artists = album.getArtists()
                .stream()
                .map(artist ->
                        new ArtistDto(artist.getId(), artist.getName()))
                .collect(Collectors.toSet());


        songs = album.getSongs().
                stream().
                map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .collect(Collectors.toSet());


        UpdateAlbumWithSongsAndArtistsResponseDto responseDto = new UpdateAlbumWithSongsAndArtistsResponseDto(
                album.getTitle(), artists, songs);

        return responseDto;
    }

    AllAlbumsResponseDto findAllAlbumsDto(Pageable pageable) {
        List<Album> all = findAllAlbums(pageable).stream().toList();
        List<AlbumDto> allAlbumsDto = all.stream()
                .map(album -> new AlbumDto(album.getId(), album.getTitle(),
                        album.getSongs()
                                .stream().map(
                                        song -> new SongDto(song.getId(), song.getName(), song.getDuration(),
                                                new GenreDto(song.getGenre().getId(), song.getGenre().getName()))
                                ).toList()))
                .collect(Collectors.toList());
        return new AllAlbumsResponseDto(allAlbumsDto);
    }

    Set<AlbumDtoWithArtistsAndSongsResponseDto> findAlbumsDtoByArtistId(final Long artistId) {
        return albumRepository.findByArtistsId(artistId).stream().map(
                album -> new AlbumDtoWithArtistsAndSongsResponseDto(
                        album.getId(),
                        album.getTitle(),
                        album.getReleaseDate(),
                        album.getArtists().stream().map(
                                        artist -> new ArtistDto(artist.getId(), artist.getName()))
                                .collect(Collectors.toSet()),
                        album.getSongs().stream().map(
                                        song -> new SongInfoDto(song.getId(), song.getName(),
                                                song.getDuration(), song.getReleaseDate(),
                                                new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                                .collect(Collectors.toSet())
                )).collect(Collectors.toSet());
    }

    void updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {

        Album oldAlbum = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + albumId + " not found"));

        if (requestDto.artistIds() != null && !requestDto.artistIds().isEmpty()) {
            requestDto.artistIds().forEach(artistId -> {
                Artist artist = artistRepository.findArtistById(artistId)
                        .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found."));
                artistService.addArtistToAlbum(artist.getId(), albumId);
            });
        }

        if (requestDto.songIds() != null && !requestDto.songIds().isEmpty()) {
            requestDto.songIds().forEach(songId -> {
                Song song = getSongFromDB(songId);
                oldAlbum.addSong(song);
            });
        }

        if (requestDto.newName() != null && !requestDto.newName().equals(oldAlbum.getTitle())) {
            oldAlbum.updateName(requestDto.newName());
        }


        albumRepository.save(oldAlbum);
    }

    UpdateSongAlbumResponseDto addSongToAlbum(final Long songId, final Long albumId) {
        Album fetchedAlbum = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + albumId + " not found."));
        fetchedAlbum.addSongToAlbum(getSongFromDB(songId));
        Album savedAlbum = albumRepository.save(fetchedAlbum);

        return new UpdateSongAlbumResponseDto("Successfully added song with id: " + songId + " to album with id: " + albumId + "."
                , new UpdateAlbumWithSongsAndArtistsResponseDto(savedAlbum.getTitle(),
                savedAlbum.getArtists().stream()
                        .map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                        .collect(Collectors.toSet()),
                savedAlbum.getSongs().stream()
                        .map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                        .collect(Collectors.toSet())
        ));
    }

    void deleteSongFromAlbums(Song song) {

        List<Album> albums = albumRepository.findBySongsId(song.getId());

        for (Album album : albums) {
            album.removeSong(song);
        }
    }

    private Song getSongFromDB(final Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + songId + " not found."));
    }
}
