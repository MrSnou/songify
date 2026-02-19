package com.songify.domain.crud;

import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static com.songify.domain.crud.SongifyCrudFacadeConfiguration.createSongifyCrudFacade;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SongifyCrudFacadeTest {

    SongifyCrudFacade songifyCrudFacade = createSongifyCrudFacade(
            new InMemorySongRepository(),
            new InMemoryGenreRepository(),
            new InMemoryArtistRepository(),
            new ImMemoryAlbumRepository()
    );

    @Test
    @DisplayName("Should return correct dto When adding new artist")
    public void should_return_correct_dto_when_adding_new_artist() {
        // Given
        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("sampleName")
                .build();
        // When
        ArtistDto responseTestDto = songifyCrudFacade.addArtist(requestDto);
        // Then
        assertThat(songifyCrudFacade.addArtist(requestDto)).isExactlyInstanceOf(ArtistDto.class);
        assertNotNull(responseTestDto);
        assertThat(responseTestDto.id()).isNotNull();
        assertThat(responseTestDto.name()).isNotNull();
    }

    @Test
    @DisplayName("Should add artist 'amigo' with id: 0 When amigo was sent")
    public void should_add_artist_amigo_with_id_0_when_amigo_was_sent() {
        // Given
        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Amigo")
                .build();
        Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists(Pageable.unpaged());
        assertTrue(allArtists.isEmpty());
        // When
        ArtistDto testResult = songifyCrudFacade.addArtist(requestDto);
        // Then
        int sizeOfDb = songifyCrudFacade.findAllArtists(Pageable.unpaged()).size();
        assertThat(sizeOfDb).isEqualTo(1);
        assertNotNull(testResult);
        assertThat(testResult.id()).isEqualTo(0L);
        assertThat(testResult.name()).isEqualTo("Amigo").isNotEqualTo("RandomName");
    }



//    @Test
//    public void second() {
//
//    }
//
//    @Test
//    public void third() {
//
//    }

}