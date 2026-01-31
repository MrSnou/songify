package com.songify.domain.crud;

import com.songify.domain.crud.Exceptions.ArtistNotFoundException;
import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        log.warn("Artist not found in db");
        return artistRepository.findArtistById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found."));
    }
}
