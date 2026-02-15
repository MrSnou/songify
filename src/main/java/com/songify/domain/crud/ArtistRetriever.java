package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.response.AllAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistWithAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.error.ArtistNotFoundException;
import com.songify.infrastructure.crud.artist.ArtistDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Log4j2
class ArtistRetriever {

    ArtistRepository artistRepository;

    Set<ArtistDto> findAllArtists(final Pageable pageable) {
        return artistRepository.findAll(pageable)
                .stream()
                .map( artist -> new ArtistDto(
                        artist.getId(), artist.getName()
                ))
                .collect(Collectors.toSet());
    }

    Artist findArtistById(final Long artistId) {
        return artistRepository.findArtistById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found."));
    }

    ArtistWithAlbumsResponseDto findArtistWithAlbumsById(final Long artistId) {
        Artist artist = artistRepository.findArtistById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found."));

        List<AlbumDto> albumDtoList = artist.getAlbums()
                .stream()
                .filter(album -> album.getArtists().contains(artist))
                .map(album -> new AlbumDto(album.getId(), album.getTitle()))
                .toList();

        ArtistWithAlbumsResponseDto responseDto = new ArtistWithAlbumsResponseDto(
                "Successfully retrieved: "+ artist.getName() + " with all it's albums.",
                new ArtistDto(artist.getId(), artist.getName()),
                new AllAlbumsResponseDto(albumDtoList)
                );

        return responseDto;
    }
}
