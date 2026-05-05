package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.dto.request.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.artist.dto.response.UpdateArtistAlbumResponseDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistWithAlbumsResponseDto;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.dto.request.GenreRequestDto;
import com.songify.infrastructure.crud.song.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongResponseDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.album.dto.request.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.infrastructure.crud.genre.dto.response.GenreWithSongsResponseDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public SongDto addSong(final SongRequestDto requestDto) {
        return songService.addSong(requestDto);
    }

    public Set<ArtistDto> findAllArtists(final Pageable pageable) {
        return artistService.findAllArtists(pageable);
    }

    public List<SongDto> findAllSongs(Pageable pageable) {
        return songService.findAll(pageable);
    }

    public SongDto findSongDtoById(Long id) {
        return songService.findSongDtoById(id);
    }

    public SongGenreDto findSongGenreDtoById(Long id) {
        return songService.findSongGenreDtoById(id);
    }

    public AlbumDtoWithArtistsAndSongsResponseDto findAlbumByIdWithArtistsAndSongs(final Long albumId) {
        return albumService.findAlbumByIdWithArtistsAndSongs(albumId);
    }

    public UpdateSongResponseDto updatesSongPartiallyById(Long id, UpdateSongRequestDto songFromRequest) {
        return songService.updateById(id, songFromRequest);
    }

    public DeleteSongResponseDto deleteSongById(Long id) {
        return songService.deleteSongById(id);
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

    public ArtistDto updateArtistNameById(Long artistId, String newName) {
        return artistService.updateArtistNameById(artistId, newName);
    }

    public ArtistDto findArtistById(final Long artistId) {
        Artist artist = artistService.findArtistById(artistId);
        return new ArtistDto(artistId, artist.getName());
    }


    public GenreDto updateGenreNameById(final Long genreId, String newName) {
        return genreService.updateGenreNameById(genreId, newName);

    }

    public UpdateAlbumWithSongsAndArtistsResponseDto updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {
        albumService.updateAlbumByIdWithSongsAndArtists(albumId, requestDto);
        return albumService.getUpdateAlbumByIdWithSongsAndArtistsResponse(albumId);
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

    public List<GenreDto> findAllGenreDto(Pageable pageable) {
        return genreService.findAllGenreDto(pageable);
    }

    public AllAlbumsResponseDto findAllAlbumDto(Pageable pageable) {
        return albumService.findAllAlbumsDto(pageable);
    }

    public GenreWithSongsResponseDto findGenreDtoWithSongsDto(final Long genreId) {
        GenreDto genreDto = genreService.findGenreDtoById(genreId);
        List<SongDto> songsDto = songService.findSongsDtoByGenreId(genreService.findGenreById(genreId));

        return new GenreWithSongsResponseDto(
                "Successfully retrieved all songs with genre: " + genreService.findGenreDtoById(genreId).name(),
                genreDto,  songsDto
                );
    }

    public ArtistWithAlbumsResponseDto findArtistDtoWithAlbumsDto(final Long artistId) {
        return artistService.findArtistWithAlbumsById(artistId);
    }

    public Set<AlbumDtoWithArtistsAndSongsResponseDto> findAlbumsByArtistId(final Long artistId) {
        return albumService.findAlbumsDtoByArtistId(artistId);
    }
}
