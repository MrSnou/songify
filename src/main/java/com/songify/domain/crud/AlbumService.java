package com.songify.domain.crud;

import com.songify.domain.crud.dto.album.AlbumDto;
import com.songify.domain.crud.dto.album.AlbumResponseDto;
import com.songify.domain.crud.dto.album.DeleteAlbumResponseDto;
import com.songify.infrastructure.crud.album.dto.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.domain.crud.dto.album.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.domain.crud.dto.album.AllAlbumsResponseDto;
import com.songify.domain.crud.exception.AlbumNotEmptyException;
import com.songify.domain.crud.exception.AlbumNotFoundException;
import com.songify.domain.crud.dto.artist.ArtistDto;
import com.songify.domain.crud.exception.ArtistNotFoundException;
import com.songify.domain.crud.dto.genre.GenreDto;
import com.songify.domain.crud.dto.song.UpdateSongAlbumResponseDto;
import com.songify.domain.crud.exception.SongNotFoundException;
import com.songify.domain.crud.dto.song.SongDto;
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
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;

    private final ArtistService artistService;


    AlbumDto addAlbumWithSong(final List<Long> songIds, final String title, final Instant releaseDate) {
        List<Song> songs = songIds.stream()
                .map(this::getSongFromDb)
                .toList();


        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        songs.forEach(album::addSongToAlbum);
        Album savedAlbum = albumRepository.save(album);
        return new AlbumDto(savedAlbum.getId(), savedAlbum.getTitle(),
                savedAlbum.getSongs()
                        .stream().map(
                                song -> new SongDto(song.getId(), song.getName(), song.getDuration(), song.getReleaseDate(),
                                        new GenreDto(song.getGenre().getId(), song.getGenre().getName()))
                        ).toList());

    }

    DeleteAlbumResponseDto deleteById(Album album) {
        if (album.getSongs().isEmpty() && album.getArtists().isEmpty()) {
            albumRepository.deleteById(album.getId().longValue());
            return DeleteAlbumResponseDto.builder()
                    .message("Album with id " + album.getId() + " successfully was deleted.")
                    .build();
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

        Set<ArtistDto> artistDtoSet = artists.stream()
                .map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                .collect(Collectors.toSet());

        Set<SongDto> songDtoSet = songs.stream()
                .map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(),
                        song.getReleaseDate(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .collect(Collectors.toSet());

        return new AlbumDtoWithArtistsAndSongsResponseDto(
                albumByIdFromDb.getId(),
                albumByIdFromDb.getTitle(),
                albumByIdFromDb.getReleaseDate(),
                artistDtoSet,
                songDtoSet
        );
    }

    Album findAlbumById(final Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found"));
    }

    AlbumDtoWithArtistsAndSongsResponseDto getUpdateAlbumByIdWithSongsAndArtistsResponse(final Long id) {
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
                map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(), song.getReleaseDate(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                .collect(Collectors.toSet());


        AlbumDtoWithArtistsAndSongsResponseDto responseDto = AlbumDtoWithArtistsAndSongsResponseDto.builder()
                .id(album.getId())
                .name(album.getTitle())
                .releaseDate(album.getReleaseDate())
                .songs(songs)
                .artists(artists)
                .build();

        return responseDto;
    }

    AllAlbumsResponseDto findAllAlbumsDto(Pageable pageable) {
        List<Album> all = findAllAlbums(pageable).stream().toList();
        List<AlbumDto> allAlbumsDto = all.stream()
                .map(album -> new AlbumDto(album.getId(), album.getTitle(),
                        album.getSongs()
                                .stream().map(
                                        song -> new SongDto(song.getId(), song.getName(), song.getDuration(), song.getReleaseDate(),
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
                                        song -> new SongDto(song.getId(), song.getName(),
                                                song.getDuration(), song.getReleaseDate(),
                                                new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                                .collect(Collectors.toSet())
                )).collect(Collectors.toSet());
    }

    AlbumResponseDto updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {

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
                Song song = getSongFromDb(songId);
                oldAlbum.addSong(song);
            });
        }

        if (requestDto.newName() != null && !requestDto.newName().equals(oldAlbum.getTitle())) {
            oldAlbum.updateName(requestDto.newName());
        }


        albumRepository.save(oldAlbum);

        return AlbumResponseDto.builder()
                .message("Album with id " + albumId + " successfully updated.")
                .album(getUpdateAlbumByIdWithSongsAndArtistsResponse(albumId))
                .build();
    }

    UpdateSongAlbumResponseDto addSongToAlbum(final Long songId, final Long albumId) {
        Album fetchedAlbum = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + albumId + " not found."));
        fetchedAlbum.addSongToAlbum(getSongFromDb(songId));
        Album savedAlbum = albumRepository.save(fetchedAlbum);

        AlbumDtoWithArtistsAndSongsResponseDto responseDto = AlbumDtoWithArtistsAndSongsResponseDto.builder()
                .id(savedAlbum.getId())
                .name(savedAlbum.getTitle())
                .releaseDate(savedAlbum.getReleaseDate())
                .songs(savedAlbum.getSongs().stream()
                        .map(song -> new SongDto(song.getId(), song.getName(), song.getDuration(), song.getReleaseDate(), new GenreDto(song.getGenre().getId(), song.getGenre().getName())))
                        .collect(Collectors.toSet()))
                .artists(savedAlbum.getArtists().stream()
                        .map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                        .collect(Collectors.toSet()))
                .build();

        return new UpdateSongAlbumResponseDto(
                "Successfully added song with id: " + songId + " to album with id: " + albumId + ".",
                responseDto);
    }

    void deleteSongFromAlbums(Song song) {

        List<Album> albums = albumRepository.findBySongsId(song.getId());

        for (Album album : albums) {
            album.removeSong(song);
        }
    }


    // Important method, direct fetch from repo because SongService needs AlbumService and vice versa.
    private Song getSongFromDb(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + songId + " not found"));
    }
}
