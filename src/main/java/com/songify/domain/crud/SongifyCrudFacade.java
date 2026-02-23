package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.dto.request.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistWithAlbumsResponseDto;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.dto.request.GenreRequestDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.album.dto.request.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.infrastructure.crud.genre.dto.response.GenreWithSongsResponseDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongAlbumRequestDto;
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
//@Transactional
public class SongifyCrudFacade {

    private final SongAdder songAdder;
    private final SongUpdater songUpdater;
    private final SongDeleter songDeleter;
    private final SongRetriever songRetriever;

    private final ArtistAdder artistAdder;
    private final ArtistUpdater artistUpdater;
    private final ArtistDeleter artistDeleter;
    private final ArtistAssigner artistAssigner;
    private final ArtistRetriever artistRetriever;

    private final GenreAdder genreAdder;
    private final GenreDeleter genreDeleter;
    private final GenreUpdater genreUpdater;
    private final GenreRetriever genreRetriever;

    private final AlbumAdder albumAdder;
    private final AlbumUpdater albumUpdater;
    private final AlbumDeleter albumDeleter;
    private final AlbumRetriever albumRetriever;

    public ArtistDto addArtist(ArtistRequestDto dto) {
        return artistAdder.addArtist(dto.name());
    }

    public GenreDto addGenre(GenreRequestDto dto) {
        return genreAdder.addGenre(dto.name());
    }

    public AlbumDto addAlbumWithSong(AlbumWithSongRequestDto dto) {
        return albumAdder.addAlbumWithSong(dto.songId(), dto.title(), dto.releaseDate());
    }

    public SongDto addSong(final SongRequestDto requestDto) {
        return songAdder.addSongFromSongDto(requestDto);
    }

    public Set<ArtistDto> findAllArtists(final Pageable pageable) {
        return artistRetriever.findAllArtists(pageable);
    }

    public List<SongDto> findAllSongs(Pageable pageable) {
        return songRetriever.findAll(pageable);
    }

    public SongDto findSongDtoById(Long id) {
        return songRetriever.findSongDtoById(id);
    }

    public SongGenreDto findSongGenreDtoById(Long id) {
        return songRetriever.findSongGenreDtoById(id);
    }

    public AlbumDtoWithArtistsAndSongsResponseDto findAlbumByIdWithArtistsAndSongs(final Long id) {
        return albumRetriever.findAlbumByIdWithArtistsAndSongs(id);
    }

    public SongDto updatesSongPartiallyById(Long id, UpdateSongRequestDto songFromRequest) {
        return songUpdater.updateById(id, songFromRequest);
    }

    public void deleteSongById(Long id) {
        songRetriever.existsById(id);
        songDeleter.deleteSongById(id);

    }

    public void deleteAlbumById(final Long requestAlbumId) {
        albumDeleter.deleteById(albumRetriever.findAlbumById(requestAlbumId));

    }

    public void deleteGenreById(final Long genreId) {
        genreDeleter.deleteGenreById(genreRetriever.findGenreById(genreId));
    }

    public void deleteArtistByIdWithAlbumsAndSongs(final Long artistId) {
        artistDeleter.deleteArtistByIdWithAlbumsAndSongs(artistId);
    }

    public void addArtistToAlbum(Long artistID, Long albumID) {
        artistAssigner.addArtistToAlbum(artistID, albumID);
    }

    public ArtistDto updateArtistNameById(Long artistId, String newName) {
        return artistUpdater.updateArtistNameById(artistId, newName);
    }

    public ArtistDto findArtistById(final Long artistId) {
        Artist artist = artistRetriever.findArtistById(artistId);
        return new ArtistDto(artistId, artist.getName());
    }

    public ArtistDto addArtistWithDefaultAlbumAndSong(ArtistRequestDto requestDto) {
        return artistAdder.addArtistWithDefaultAlbumAndSong(requestDto);
    }


    public GenreDto updateGenreNameById(final Long genreId, String newName) {
        return genreUpdater.updateGenreNameById(genreId, newName);

    }

    public UpdateAlbumWithSongsAndArtistsResponseDto updateAlbumByIdWithSongsAndArtists(final Long albumId, UpdateAlbumWithSongsAndArtistsRequestDto requestDto) {
        albumUpdater.updateAlbumByIdWithSongsAndArtists(albumId, requestDto);
        UpdateAlbumWithSongsAndArtistsResponseDto responseDto = albumRetriever.getUpdateAlbumByIdWithSongsAndArtistsResponse(albumId);
        return responseDto;
    }

    public UpdateSongAlbumResponseDto updateSongAlbum(final Long songId, final UpdateSongAlbumRequestDto request) {
        UpdateSongAlbumResponseDto responseDto = songUpdater.updateSongAlbumById(songId, request);
        return responseDto;
    }

    public void updateSongGenreById(final Long songId, final Long genreId) {
        songUpdater.updateSongGenreById(songId, genreId);
    }

    public GenreDto findGenreDtoById(final Long genreId) {
        return genreRetriever.findGenreDtoById(genreId);
    }

    public List<GenreDto> findAllGenreDto(Pageable pageable) {
        return genreRetriever.findAllGenreDto(pageable);
    }

    public List<AlbumDto> findAllAlbumDto(Pageable pageable) {
        return albumRetriever.findAllAlbumsDto(pageable);
    }

    public GenreWithSongsResponseDto findGenreDtoWithSongsDto(final Long genreId) {
        GenreDto genreDto = genreRetriever.findGenreDtoById(genreId);
        List<SongDto> songsDto = songRetriever.findSongsDtoByGenreId(genreRetriever.findGenreById(genreId));

        return new GenreWithSongsResponseDto(
                "Successfully retrieved all songs with genre: " + genreRetriever.findGenreDtoById(genreId).name(),
                genreDto,  songsDto
                );
    }

    public ArtistWithAlbumsResponseDto findArtistDtoWithAlbumsDto(final Long artistId) {
        ArtistWithAlbumsResponseDto responseDto = artistRetriever.findArtistWithAlbumsById(artistId);
        return responseDto;
    }

    public Set<AlbumDtoWithArtistsAndSongsResponseDto> findAlbumsByArtistId(final Long artistId) {
        return albumRetriever.findAlbumsDtoByArtistId(artistId);
    }
}
