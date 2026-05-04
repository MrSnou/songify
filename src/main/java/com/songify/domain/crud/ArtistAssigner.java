package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.response.UpdateArtistAlbumResponseDto;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.song.util.SongDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
class ArtistAssigner {

    private final AlbumRetriever albumRetriever;
    private final ArtistRetriever artistRetriever;

    UpdateArtistAlbumResponseDto addArtistToAlbum(final Long artistID, final Long albumID) {
        Artist artist = artistRetriever.findArtistById(artistID);
        Album album = albumRetriever.findAlbumById(albumID);
        artist.addAlbum(album);
        return UpdateArtistAlbumResponseDto.builder()
                .message("Successfully added artist [" + artist.getId() + "] " + artist.getName() +
                        " to album [" + album.getId() + "] " + album.getTitle() + ".")
                .artist(new ArtistDto(artist.getId(), artist.getName()))
                .album(new AlbumDto(album.getId(), album.getTitle(), album.getSongs()
                        .stream().map(
                                song -> new SongDto(song.getId(), song.getName(), song.getDuration(),
                                        new GenreDto(song.getGenre().getId(), song.getGenre().getName()))
                                ).toList()
                        ))
                .build();
    }
}
