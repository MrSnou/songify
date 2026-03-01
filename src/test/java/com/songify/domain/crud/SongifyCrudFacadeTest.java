package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.request.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.error.AlbumNotFoundException;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.artist.error.ArtistNotFoundException;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.dto.request.GenreRequestDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import com.songify.infrastructure.crud.song.error.SongNotFoundException;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongLanguageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.songify.domain.crud.SongifyCrudFacadeConfiguration.createSongifyCrudFacade;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SongifyCrudFacadeTest {

    SongifyCrudFacade songifyCrudFacade = createSongifyCrudFacade(
            new InMemorySongRepository(),
            new InMemoryGenreRepository(),
            new InMemoryArtistRepository(),
            new InMemoryAlbumRepository()
    );

    @BeforeEach
    void setUp() {
        /// In main program there is always "Default" genre in db with id 1L
        GenreRequestDto defaultGenre = GenreRequestDto.builder()
                .name("Default")
                .build();
        songifyCrudFacade.addGenre(defaultGenre);
    }

    @Nested
    @DisplayName("AddArtist - Tests")
    class AddArtistTests {
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
            assertThat(responseTestDto).isExactlyInstanceOf(ArtistDto.class);
            assertThat(responseTestDto)
                    .isNotNull()
                    .extracting(ArtistDto::id, ArtistDto::name)
                    .doesNotContainNull();
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
            assertThat(testResult)
                    .isExactlyInstanceOf(ArtistDto.class)
                    .extracting(ArtistDto::id)
                    .isEqualTo(0L)
                    .isNotNull();
            assertThat(testResult)
                    .extracting(ArtistDto::name)
                    .isEqualTo("Amigo")
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("AddGenre - Tests")
    class AddGenreTests {

        @Test
        @DisplayName("Should add genre 'TestGenre' with id: 0 When genre was sent")
        public void should_add_genre_with_id_0_when_genre_was_sent() {
            // Given
            GenreRequestDto requestDto = GenreRequestDto.builder()
                    .name("TestGenre")
                    .build();
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(0);
            // When
            GenreDto genreDto = songifyCrudFacade.addGenre(requestDto);
            // Then
            assertThat(songifyCrudFacade.findGenreDtoById(1L));
            assertThat(genreDto).isExactlyInstanceOf(GenreDto.class)
                    .extracting(GenreDto::id)
                    .isEqualTo(2L)
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("AddAlbumWithSongs - Tests")
    class AddAlbumWithSongsTests {

        @Test
        @DisplayName("Should add Album With Song/s")
        public void should_add_album_with_song_when_album_was_sent_to_empty_db() {
            // Given
            SongRequestDto songDto = SongRequestDto.builder()
                    .name("TestSong")
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
            songifyCrudFacade.addSong(songDto);
            /// Check is song was correctly added
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            AlbumWithSongRequestDto requestDto = new AlbumWithSongRequestDto("TestAlbum", Instant.now(), 0L);
            // When
            AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(requestDto);
            // Then
            assertThat(albumDto).isNotNull();
            assertThat(albumDto).isExactlyInstanceOf(AlbumDto.class);
            assertThat(albumDto).extracting(AlbumDto::id).isEqualTo(0L);
        }

        @Test
        @DisplayName("Should throw exception when album with id of non existent song/s was sent")
        public void should_throw_SongNotFoundException_when_album_without_Song_was_sent_to_empty_db() {
            // Given
            AlbumWithSongRequestDto requestDto = new AlbumWithSongRequestDto("TestAlbum", Instant.now(), 0L);
            // When
            Throwable songException = catchThrowable(() -> songifyCrudFacade.addAlbumWithSong(requestDto));
            // Then
            assertThat(songException).isExactlyInstanceOf(SongNotFoundException.class);
            assertThat(songException.getMessage()).isEqualTo("Song with id " + 0L + " not found");
        }
    }

    @Nested
    @DisplayName("AddSong - tests")
    class AddSongsTests {

        @Test
        @DisplayName("Should add song When song was sent")
        public void should_add_Song_When_song_was_sent() {
            // Given
            SongRequestDto songRequest = SongRequestDto.builder()
                    .name("TestSong")
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
            // When
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            // Then
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id())).extracting(SongDto::name)
                    .isEqualTo(addedSong.name())
                    .isNotNull();
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id()))
                    .extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(
                            addedSong.id(),
                            addedSong.name(),
                            addedSong.duration()
                    );

        }
    }

    @Nested
    @DisplayName("findAllArtists - Tests")
    class FindAllArtistsTests {

        @Test
        @DisplayName("Should return empty list When method was called")
        void should_return_empty_list_When_method_was_called() {
            // Given
            // When
            Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists(Pageable.unpaged());
            // Then
            assertThat(allArtists).isNotNull();
            assertThat(allArtists.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return list of artists When method was called")
        void should_return_list_of_artists_When_method_was_called() {
            // Given
            /// Adding 2 test Artists to db, so it's not empty
            ArtistRequestDto testArtist1 = ArtistRequestDto.builder()
                    .name("TestArtist_1")
                    .build();
            ArtistRequestDto testArtist2 = ArtistRequestDto.builder()
                    .name("TestArtist_2")
                    .build();

            songifyCrudFacade.addArtist(testArtist1);
            songifyCrudFacade.addArtist(testArtist2);
            // When
            Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists(Pageable.unpaged());
            // Then
            assertThat(allArtists).isNotNull();
            assertThat(allArtists.size()).isEqualTo(2);
        }
    }


    @Nested
    @DisplayName("FindAllSongs - Tests")
    class FindAllSongsTests {

        @Test
        @DisplayName("Should return empty list of songs When method was called")
        void should_return_empty_list_of_songs_When_method_was_called() {
            // Given
            // When
            Set<SongDto> allSongs = new HashSet<>(songifyCrudFacade.findAllSongs(Pageable.unpaged()));
            // Then
            assertThat(allSongs).isNotNull();
            assertThat(allSongs.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return list with songs When method was called")
        void should_return_list_with_songs_When_method_was_called() {
            // Given
            SongRequestDto testSong_1 = SongRequestDto.builder()
                    .name("TestSong_1")
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();

            SongRequestDto testSong_2 = SongRequestDto.builder()
                    .name("TestSong_2")
                    .duration(321L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
            songifyCrudFacade.addSong(testSong_1);
            songifyCrudFacade.addSong(testSong_2);
            /// Checking if songs was correctly added to db
            // When
            Set<SongDto> allSongs = new HashSet<>(songifyCrudFacade.findAllSongs(Pageable.unpaged()));
            // Then
            assertThat(allSongs).isNotNull();
            assertThat(allSongs.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("FindSongDtoById - Tests")
    class FindSongDtoByIdTests {

        @Test
        @DisplayName("Should throw SongNotFoundException When incorrect DTO was sent")
        void should_throw_SongNotFoundException_When_incorrect_DTO_was_sent() {
            // Given
            // When
            Throwable songException = catchThrowable(() -> songifyCrudFacade.findSongDtoById(Long.MAX_VALUE));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class);
            assertThat(songException.getMessage()).isEqualTo("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return song with id: 0 When method was called")
        void should_return_song_with_id_When_method_was_called() {
            //Given
            SongRequestDto songRequest = SongRequestDto.builder()
                    .name("TestSong_1")
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            /// Checking if song was correctly added to db
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            SongDto songDtoById = songifyCrudFacade.findSongDtoById(addedSong.id());
            // Then
            assertThat(songDtoById).isNotNull().isExactlyInstanceOf(SongDto.class);
            assertThat(songDtoById).extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(0L, "TestSong_1", 123L);
        }
    }

    @Nested
    @DisplayName("FindSongGenreDtoById - Tests")
    class FindSongGenreDtoByIdTests {

        @Test
        @DisplayName("Should return SongNotFoundException When song was not found")
        void should_throw_GenreNotFoundException_When_song_was_not_found() {
            // Given
            // When
            Throwable genreException = catchThrowable(() -> songifyCrudFacade.findSongGenreDtoById(Long.MAX_VALUE));
            // Then
            assertThat(genreException).isInstanceOf(SongNotFoundException.class);
            assertThat(genreException.getMessage()).isEqualTo("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return Default genre When song without genre was requested at method call")
        void should_return_Default_genre_When_song_without_genre_was_requested_at_method_call() {
            // Given
            SongRequestDto songRequest = SongRequestDto.builder()
                    .name("TestSong_1")
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            /// Checking if song was correctly added to db
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id())).isEqualTo(addedSong);
            // When
            SongGenreDto songGenreDtoById = songifyCrudFacade.findSongGenreDtoById(addedSong.id());
            // Then
            assertThat(songGenreDtoById).isNotNull().isExactlyInstanceOf(SongGenreDto.class);
            assertThat(songGenreDtoById.genre()).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(null, "Default");

        }

        @Test
        @DisplayName("Should return Genre When method with correct song was called")
        void should_return_Genre_When_method_with_correct_song_was_called() {
            // Given
            SongRequestDto songRequest = SongRequestDto.builder()
                    .name("TestSong_1")
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
            GenreRequestDto genreRequest = GenreRequestDto.builder()
                    .name("TestGenre_1")
                    .build();
            GenreDto genreDto = songifyCrudFacade.addGenre(genreRequest);
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            songifyCrudFacade.updateSongGenreById(addedSong.id(), genreDto.id());
            /// Checking if song and genre was correctly added to db
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findGenreDtoById(genreDto.id())).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(2L, "TestGenre_1");
            // When
            /// "(addedSong.id() + 1)" because this is testing db (HashSet) with Atomic Integer don't have update function like postgreSQL
            /// and have to remove old and add new what affects ID, in main DB this is not a problem because custom update JPQL query
            SongGenreDto songGenreDtoById = songifyCrudFacade.findSongGenreDtoById(addedSong.id() + 1);
            // Then
            assertThat(songGenreDtoById).isNotNull().isExactlyInstanceOf(SongGenreDto.class);
            assertThat(songGenreDtoById).extracting(SongGenreDto::songId, SongGenreDto::genre)
            .containsExactly(1L, genreDto);
        }
    }

    @Nested
    @DisplayName("FindAlbumByIdWithArtistsAndSongs - Tests")
    class FindAlbumByIdWithArtistsAndSongsTests {

        @Test
        @DisplayName("Should return AlbumNotFoundException When method was called with incorrect albumId")
        void should_return_AlbumNotFoundException_When_method_was_called_with_incorrect_albumId() {
            // Given
            // When
            Throwable albumException = catchThrowable(() -> songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(Long.MAX_VALUE));
            // Then
            assertThat(albumException).isInstanceOf(AlbumNotFoundException.class);
            assertThat(albumException.getMessage()).isEqualTo("Album with id: " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return Album When method was called")
        void should_return_Album_When_method_was_called() {
            // Given
            SongRequestDto songDto = new SongRequestDto("TestSong", 123L, Instant.now(), SongLanguageDto.ENGLISH);
            SongDto addedSong = songifyCrudFacade.addSong(songDto);
            /// New album with song test song
            AlbumWithSongRequestDto albumWithSongRequestDto = new AlbumWithSongRequestDto("TestAlbum", Instant.now(), addedSong.id());
            AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(albumWithSongRequestDto);
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);

            // When
            songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedSong.id());
            // Then

        }
    }



    @Nested
    @DisplayName("DeleteArtistByIdWithAlbumsAndSongs - Tests")
    class DeleteArtistByIdWithAlbumsAndSongsTests {

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

            /// New song because we need it for album
            SongRequestDto songDto = new SongRequestDto("TestSong", 123L, Instant.now(), SongLanguageDto.ENGLISH);
            SongDto addedSong = songifyCrudFacade.addSong(songDto);
            /// New album with song test song
            AlbumWithSongRequestDto albumWithSongRequestDto = new AlbumWithSongRequestDto("TestAlbum", Instant.now(), addedSong.id());
            AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(albumWithSongRequestDto);
            /// Adding album to artist and then checking is it's saved
            songifyCrudFacade.addArtistToAlbum(artistDto.id(), albumDto.id());
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id()).id()).isEqualTo(0L);
            assertThat(songifyCrudFacade.findAlbumsByArtistId(albumDto.id()).size()).isEqualTo(1);
            // When
            songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistDto.id());
            // Then
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
            /// Check if albums and songs are correctly deleted from db after deleting artist **/
            Throwable songThrowable = catchThrowable(() -> songifyCrudFacade.findSongDtoById(0L));
            Throwable albumThrowable = catchThrowable(() -> songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(0L));
            assertThat(songifyCrudFacade.findAlbumsByArtistId(albumDto.id()).size()).isEqualTo(0);
            assertThat(songThrowable).isExactlyInstanceOf(SongNotFoundException.class);
            assertThat(albumThrowable).isExactlyInstanceOf(AlbumNotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception artist not found when id was one")
        public void should_throw_exception_artist_not_found_when_id_was_zero() {
            // Given
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isNotNull();
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
            // When
            Throwable throwable = catchThrowable(() ->
                    songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L));
            // Then
            assertThat(throwable).isExactlyInstanceOf(ArtistNotFoundException.class);
            assertThat(throwable.getMessage()).isEqualTo("Artist with id 0 not found.");
        }
    }
}