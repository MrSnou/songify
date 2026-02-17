package com.songify.domain.crud;

import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void first() {

        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Amigo")
                .build();
        ArtistDto responseTestDto = songifyCrudFacade.addArtist(requestDto);

        assertNotNull(responseTestDto);

        assertThat(responseTestDto.id()).isEqualTo(0L);
        assertThat(responseTestDto.name()).isEqualTo("Amigo").isNotEqualTo("RandomName");
    }

    @Test
    public void first_variant_B() {

        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Shawn Mendes")
                .build();
        ArtistDto responseTestDto = songifyCrudFacade.addArtist(requestDto);

        assertNotNull(responseTestDto);

        assertThat(responseTestDto.id()).isEqualTo(0L);
        assertThat(responseTestDto.name()).isEqualTo("Shawn Mendes").isNotEqualTo("RandomName");
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