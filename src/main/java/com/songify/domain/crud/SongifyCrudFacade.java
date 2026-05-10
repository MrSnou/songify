package com.songify.domain.crud;

import com.songify.domain.crud.dto.album.AlbumDto;
import com.songify.domain.crud.dto.album.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.domain.crud.dto.album.AlbumResponseDto;
import com.songify.domain.crud.dto.artist.AllArtistDto;
import com.songify.domain.crud.dto.artist.ArtistUpdateResponseDto;
import com.songify.domain.crud.dto.genre.AllGenresDto;
import com.songify.domain.crud.dto.genre.GenreResponseDto;
import com.songify.domain.crud.dto.song.AllSongsDto;
import com.songify.domain.crud.dto.song.CreateSongDto;
import com.songify.domain.crud.dto.album.AlbumWithSongRequestDto;
import com.songify.domain.crud.dto.album.AllAlbumsResponseDto;
import com.songify.domain.crud.dto.artist.ArtistDto;
import com.songify.domain.crud.dto.artist.ArtistRequestDto;
import com.songify.domain.crud.dto.artist.UpdateArtistAlbumResponseDto;
import com.songify.domain.crud.dto.artist.ArtistWithAlbumsResponseDto;
import com.songify.domain.crud.dto.genre.GenreDto;
import com.songify.domain.crud.dto.genre.GenreRequestDto;
import com.songify.domain.crud.dto.song.UpdateSongResponseDto;
import com.songify.domain.crud.dto.song.SongDto;
import com.songify.domain.crud.dto.song.SongRequestDto;
import com.songify.domain.crud.dto.album.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.domain.crud.dto.genre.GenreWithSongsResponseDto;
import com.songify.domain.crud.dto.song.UpdateSongRequestDto;
import com.songify.domain.crud.dto.song.UpdateSongAlbumResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SongifyCrudFacade {

    private final SongService songService;
    private final AlbumService albumService;
    private final GenreService genreService;
    private final ArtistService artistService;


    public ArtistDto addArtist(ArtistRequestDto dto) {
        return artistService.addArtist(dto.name());
    }

    public GenreDto addGenre(GenreRequestDto dto) {
        return genreService.addGenre(dto.name());
    }

    public AlbumDto addAlbumWithSong(AlbumWithSongRequestDto dto) {
        return albumService.addAlbumWithSong(dto.songIds(), dto.title(), dto.releaseDate());
    }

    public CreateSongDto addSong(final SongRequestDto requestDto) {
        return CreateSongDto.builder()
                .song(songService.addSong(requestDto))
                .build();
    }

    public AllArtistDto findAllArtists(final Pageable pageable) {
        return artistService.findAllArtists(pageable);
    }

    public AllSongsDto findAllSongDto(Pageable pageable) {
        List<SongDto> allSongs = findAllSongs(pageable);
        return AllSongsDto.builder()
                .songs(allSongs)
                .build();
    }

    List<SongDto> findAllSongs(Pageable pageable) {
        return songService.findAll(pageable);
    }

    public SongDto findSongDtoById(Long id) {
        return songService.findSongDtoById(id);
    }

    public AlbumDtoWithArtistsAndSongsResponseDto findAlbumByIdWithArtistsAndSongs(final Long albumId) {
        return albumService.findAlbumByIdWithArtistsAndSongs(albumId);
    }

    public UpdateSongResponseDto updatesSongPartiallyById(Long id, UpdateSongRequestDto songFromRequest) {
        return songService.updateById(id, songFromRequest);
    }

    public void deleteSongById(Long id) {
        songService.deleteSongById(id);
    }

    public void deleteAlbumById(final Long requestAlbumId) {
        albumService.deleteById(albumService.findAlbumById(requestAlbumId));

    }

    public void deleteGenreById(final Long genreId) {
        genreService.deleteGenreById(genreService.findGenreById(genreId));
    }

    public void deleteArtistByIdWithAlbumsAndSongs(final Long artistId) {
        artistService.deleteArtistByIdWithAlbumsAndSongs(artistId);
    }

    public UpdateArtistAlbumResponseDto addArtistToAlbum(Long artistID, Long albumID) {
        return artistService.addArtistToAlbum(artistID, albumID);
    }

    public ArtistUpdateResponseDto updateArtistNameById(Long artistId, String newName) {
        return artistService.updateArtistNameById(artistId, newName);
    }

    public ArtistDto findArtistById(final Long artistId) {
        return artistService.findArtistDtoById(artistId);
    }


    public GenreResponseDto updateGenreNameById(final Long genreId, String newName) {
        return genreService.updateGenreNameById(genreId, newName);

    }

    public AlbumResponseDto updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {
        return albumService.updateAlbumByIdWithSongsAndArtists(albumId, requestDto);
    }

    public UpdateSongAlbumResponseDto updateSongAlbum(final Long songId, final Long albumId) {
        return songService.updateSongAlbumById(songId, albumId);
    }

    public UpdateSongResponseDto updateSongGenreById(final Long songId, final Long genreId) {
        return songService.updateSongGenreById(songId, genreId);
    }

    public GenreDto findGenreDtoById(final Long genreId) {
        return genreService.findGenreDtoById(genreId);
    }

    public AllGenresDto findAllGenreDto(Pageable pageable) {
        return genreService.findAllGenreDto(pageable);
    }

    public AllAlbumsResponseDto findAllAlbumDto(Pageable pageable) {
        return albumService.findAllAlbumsDto(pageable);
    }

    public GenreWithSongsResponseDto findGenreDtoWithSongsDto(final Long genreId) {
        Genre genreById = genreService.findGenreById(genreId);
        List<SongDto> songsDto = songService.findSongsDtoByGenreId(genreById);
        return GenreWithSongsResponseDto.builder()
                .genreDto(new GenreDto(genreById.getId(), genreById.getName()))
                .songs(songsDto)
                .build();


    }
    public ArtistWithAlbumsResponseDto findArtistDtoWithAlbumsDto(final Long artistId) {
        return artistService.findArtistWithAlbumsById(artistId);
    }
}
