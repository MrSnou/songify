package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.request.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.error.AlbumNotFoundException;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.artist.error.ArtistNotFoundException;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.song.error.SongNotFoundException;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongLanguageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Set;

import static com.songify.domain.crud.SongifyCrudFacadeConfiguration.createSongifyCrudFacade;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class SongifyCrudFacadeTest {

    SongifyCrudFacade songifyCrudFacade = createSongifyCrudFacade(
            new InMemorySongRepository(),
            new InMemoryGenreRepository(),
            new InMemoryArtistRepository(),
            new InMemoryAlbumRepository()
    );

    // SongifyCrudFacase addArtist() - Tests
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

    // SongifyCrudFacase deleteArtistByIdWithAlbumsAndSongs - Tests
    @Test
    @DisplayName("Should throw exception artist not found when id was one")
    public void should_throw_exception_artist_not_found_when_id_was_zero() {
        // Given
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isNotNull();
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).equals(0));
        // When
        Throwable throwable = catchThrowable(() ->
                songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L));
        // Then
        assertThat(throwable).isExactlyInstanceOf(ArtistNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Artist with id 0 not found.");
    }

    @Test
    @DisplayName("Should add and delete artist by id when he had no albums Then db size should be empty")
    public void should_add_and_delete_artist_by_id_when_he_had_no_albums_Then_db_size_should_be_empty() {
        // Given
        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Amigo")
                .build();
        ArtistDto artistDto = songifyCrudFacade.addArtist(requestDto);
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
        Set<AlbumDtoWithArtistsAndSongsResponseDto> albumsByArtistId = songifyCrudFacade.findAlbumsByArtistId(artistDto.id());
        assertThat(albumsByArtistId.size()).isEqualTo(0);
        // When
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistDto.id());
        // Then
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should delete artist by id when he have one album with song Then db size should be empty")
    public void Should_delete_artist_by_id_when_he_have_one_album_with_song_Then_db_size_should_be_empty() {
        // Given
        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Amigo")
                .build();
        ArtistDto artistDto = songifyCrudFacade.addArtist(requestDto);
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);

        Set<AlbumDtoWithArtistsAndSongsResponseDto> albumsByArtistId = songifyCrudFacade.findAlbumsByArtistId(artistDto.id());
        assertThat(albumsByArtistId.size()).isEqualTo(0);

        Instant date =  Instant.now();
        // New song because we need it for album
        SongRequestDto songDto = new SongRequestDto("TestSong", 123L, date, SongLanguageDto.ENGLISH);
        SongDto addedSong = songifyCrudFacade.addSong(songDto);
        // New album with song test song
        AlbumWithSongRequestDto albumWithSongRequestDto = new AlbumWithSongRequestDto("TestAlbum", date, addedSong.id());
        AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(albumWithSongRequestDto);
        // Adding album to artist and then checking if it's saved.
        songifyCrudFacade.addArtistToAlbum(artistDto.id(), albumDto.id());
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findSongDtoById(addedSong.id()).id()).isEqualTo(0L);
        assertThat(songifyCrudFacade.findAlbumsByArtistId(albumDto.id()).size()).isEqualTo(1);
        // When
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistDto.id());
        // Then
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
        // Check if albums and songs are correctly deleted from db after deleting artist.
        Throwable songThrowable = catchThrowable(() -> songifyCrudFacade.findSongDtoById(0L));
        Throwable albumThrowable = catchThrowable(() -> songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(0L));
        assertThat(songifyCrudFacade.findAlbumsByArtistId(albumDto.id()).size()).isEqualTo(0);
        assertThat(songThrowable).isExactlyInstanceOf(SongNotFoundException.class);
        assertThat(albumThrowable).isExactlyInstanceOf(AlbumNotFoundException.class);
    }




}