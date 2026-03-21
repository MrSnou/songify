package com.songify.domain.crud;

import com.songify.infrastructure.crud.album.AlbumDto;
import com.songify.infrastructure.crud.album.dto.request.AlbumWithSongRequestDto;
import com.songify.infrastructure.crud.album.dto.request.UpdateAlbumWithSongsAndArtistsRequestDto;
import com.songify.infrastructure.crud.album.dto.response.AlbumDtoWithArtistsAndSongsResponseDto;
import com.songify.infrastructure.crud.album.dto.response.UpdateAlbumWithSongsAndArtistsResponseDto;
import com.songify.infrastructure.crud.album.error.AlbumNotEmptyException;
import com.songify.infrastructure.crud.album.error.AlbumNotFoundException;
import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import com.songify.infrastructure.crud.artist.dto.response.ArtistWithAlbumsResponseDto;
import com.songify.infrastructure.crud.artist.error.ArtistNotFoundException;
import com.songify.infrastructure.crud.genre.GenreDto;
import com.songify.infrastructure.crud.genre.dto.request.GenreRequestDto;
import com.songify.infrastructure.crud.genre.dto.request.UpdateGenreDto;
import com.songify.infrastructure.crud.genre.dto.response.GenreWithSongsResponseDto;
import com.songify.infrastructure.crud.genre.error.GenreIsUsedBySongsException;
import com.songify.infrastructure.crud.genre.error.GenreNotFoundException;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongAlbumRequestDto;
import com.songify.infrastructure.crud.song.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.dto.response.SongGenreDto;
import com.songify.infrastructure.crud.song.dto.response.SongWithGenreResponseDto;
import com.songify.infrastructure.crud.song.dto.response.UpdateSongAlbumResponseDto;
import com.songify.infrastructure.crud.song.error.SongNotFoundException;
import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongInfoDto;
import com.songify.infrastructure.crud.song.util.SongLanguageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.songify.domain.crud.SongifyCrudFacadeConfiguration.createSongifyCrudFacade;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


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

    static class TestEntityFactory {

        private static final String DEFAULT_SONG = "TestSong";
        private static final String DEFAULT_ARTIST = "TestArtist";
        private static final String DEFAULT_GENRE = "TestGenre";
        private static final String DEFAULT_ALBUM = "TestAlbum";

        static SongRequestDto aSong() {
            return aSong(DEFAULT_SONG);
        }

        static SongRequestDto aSong(String songName) {
            return SongRequestDto.builder()
                    .name(songName)
                    .duration(123L)
                    .releaseDate(Instant.now())
                    .language(SongLanguageDto.OTHER)
                    .build();
        }

        static ArtistRequestDto anArtist() {
            return anArtist(DEFAULT_ARTIST);
        }

        static ArtistRequestDto anArtist(String artistName) {
            return ArtistRequestDto.builder()
                    .name(artistName)
                    .build();
        }

        static GenreRequestDto aGenre() {
            return aGenre(DEFAULT_GENRE);
        }

        static GenreRequestDto aGenre(String genreName) {
            return GenreRequestDto.builder()
                    .name(genreName)
                    .build();
        }

        static AlbumWithSongRequestDto anAlbumWithSong(SongDto songDto) {
            return anAlbumWithSong(DEFAULT_ALBUM, songDto);
        }

        static AlbumWithSongRequestDto anAlbumWithSong(String albumName, SongDto songDto) {
            return AlbumWithSongRequestDto.builder()
                    .title(albumName)
                    .songId(songDto.id())
                    .releaseDate(Instant.now())
                    .build();
        }
    }

    @Nested
    @DisplayName("AddArtist - Tests")
    class AddArtistTests {
        @Test
        @DisplayName("Should return correct dto When adding new artist")
        public void should_return_correct_dto_when_adding_new_artist() {
            // Given
            ArtistRequestDto artist = TestEntityFactory.anArtist();
            // When
            ArtistDto addedArtist = songifyCrudFacade.addArtist(artist);
            // Then
            assertThat(addedArtist)
                    .extracting(ArtistDto::id, ArtistDto::name)
                    .containsExactly(addedArtist.id(), addedArtist.name());
        }

        @Test
        @DisplayName("Should add artist 'amigo' with id: 0 When amigo was sent")
        public void should_add_artist_amigo_with_id_0_when_amigo_was_sent() {
            // Given
            ArtistRequestDto requestDto = TestEntityFactory.anArtist("Amigo");
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
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
        @DisplayName("Should add genre 'TestGenre' with id: 1 When genre was sent")
        public void should_add_genre_with_id_1_when_genre_was_sent() {
            // Given
            GenreRequestDto requestDto = TestEntityFactory.aGenre("TestGenre");
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            GenreDto addedGenre = songifyCrudFacade.addGenre(requestDto);
            // Then
            assertThat(songifyCrudFacade.findGenreDtoById(1L));
            assertThat(addedGenre).isExactlyInstanceOf(GenreDto.class)
                    .extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(addedGenre.id(), addedGenre.name());
        }
    }

    @Nested
    @DisplayName("AddAlbumWithSongs - Tests")
    class AddAlbumWithSongsTests {

        @Test
        @DisplayName("Should throw SongNotFoundException When empty album was sent to db")
        public void should_throw_SongNotFoundException_when_album_without_Song_was_sent_to_empty_db() {
            // Given
            AlbumWithSongRequestDto requestDto = new AlbumWithSongRequestDto("TestAlbum", Instant.now(), 0L);
            // When
            Throwable songException = catchThrowable(() -> songifyCrudFacade.addAlbumWithSong(requestDto));
            // Then
            assertThat(songException).isExactlyInstanceOf(SongNotFoundException.class);
            assertThat(songException.getMessage()).isEqualTo("Song with id " + 0L + " not found");
        }

        @Test
        @DisplayName("Should add Album With Song/s")
        public void should_add_album_with_song_when_album_was_sent_to_empty_db() {
            // Given
            SongDto songRequest = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong"));
            AlbumWithSongRequestDto albumRequest = TestEntityFactory.anAlbumWithSong("TestAlbum", songRequest);
            /// Check is song was correctly added
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            AlbumDto fetchedAlbumWithSong = songifyCrudFacade.addAlbumWithSong(albumRequest);
            // Then
            assertThat(fetchedAlbumWithSong).extracting(AlbumDto::id, AlbumDto::title).
                    containsExactly(0L, "TestAlbum");
        }


    }

    @Nested
    @DisplayName("AddSong - tests")
    class AddSongsTests {

        @Test
        @DisplayName("Should add song When song was sent")
        public void should_add_Song_When_song_was_sent() {
            // Given
            SongRequestDto songRequest = TestEntityFactory.aSong();
            // When
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            // Then
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id()))
                    .extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(0L, songRequest.name(), songRequest.duration());
        }
    }

    @Nested
    @DisplayName("FindAllArtists - Tests")
    class FindAllArtistsTests {

        @Test
        @DisplayName("Should return empty list When method was called")
        void should_return_empty_list_When_method_was_called() {
            // Given
            // When
            Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists(Pageable.unpaged());
            // Then
            assertThat(allArtists.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return list of artists When method was called")
        void should_return_list_of_artists_When_method_was_called() {
            // Given
            /// Adding 2 test Artists to db, so it's not empty
            ArtistDto addedArtist_1 = songifyCrudFacade.addArtist(TestEntityFactory.anArtist("TestArtist_1"));
            ArtistDto addedArtist_2 = songifyCrudFacade.addArtist(TestEntityFactory.anArtist("TestArtist_2"));
            // When
            Set<ArtistDto> allArtistsList = songifyCrudFacade.findAllArtists(Pageable.unpaged());
            // Then
            assertThat(allArtistsList.size()).isEqualTo(2);
            assertThat(allArtistsList.contains(addedArtist_1)).isTrue();
            assertThat(allArtistsList.contains(addedArtist_2)).isTrue();
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
            assertThat(allSongs.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return list with songs When method was called")
        void should_return_list_with_songs_When_method_was_called() {
            // Given
            SongDto addedSong_1 = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong_1"));
            SongDto addedSong_2 = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong_2"));
            /// Checking if songs was correctly added to db
            // When
            Set<SongDto> allSongs = new HashSet<>(songifyCrudFacade.findAllSongs(Pageable.unpaged()));
            // Then
            assertThat(allSongs.size()).isEqualTo(2);
            assertThat(allSongs.contains(addedSong_1)).isTrue();
            assertThat(allSongs.contains(addedSong_2)).isTrue();
        }
    }

    @Nested
    @DisplayName("FindSongDtoById - Tests")
    class FindSongDtoByIdTests {

        @Test
        @DisplayName("Should throw SongNotFoundException ")
        void should_throw_SongNotFoundException_When_song_was_not_found() {
            // Given
            // When
            Throwable songException = catchThrowable(() -> songifyCrudFacade.findSongDtoById(Long.MAX_VALUE));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return song with id: 0")
        void should_return_song_with_id_When_method_was_called() {
            //Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            /// Checking if song was correctly added to db
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            SongDto songDtoById = songifyCrudFacade.findSongDtoById(addedSong.id());
            // Then
            assertThat(songDtoById).extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(addedSong.id(), addedSong.name(), addedSong.duration());
        }
    }

    @Nested
    @DisplayName("FindSongGenreDtoById - Tests")
    class FindSongGenreDtoByIdTests {

        @Test
        @DisplayName("Should return SongNotFoundException")
        void should_throw_GenreNotFoundException_When_genre_was_not_found() {
            // Given
            // When
            Throwable genreException = catchThrowable(() -> songifyCrudFacade.findSongGenreDtoById(Long.MAX_VALUE));
            // Then
            assertThat(genreException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return Default genre When song without genre was requested at method call")
        void should_return_Default_genre_When_song_without_genre_was_requested_at_method_call() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            GenreDto defaultGenre = songifyCrudFacade.findGenreDtoById(1L);
            /// Checking if song was correctly added to db
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            SongGenreDto songGenreDtoById = songifyCrudFacade.findSongGenreDtoById(addedSong.id());
            // Then
            assertThat(songGenreDtoById).isNotNull().isExactlyInstanceOf(SongGenreDto.class);
            assertThat(songGenreDtoById.genre()).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(defaultGenre.id(), defaultGenre.name());

        }

        @Test
        @DisplayName("Should return Genre When method with correct song was called")
        void should_return_Genre_When_method_with_correct_song_was_called() {
            // Given
            SongRequestDto songRequest = TestEntityFactory.aSong();
            GenreRequestDto genreRequest = TestEntityFactory.aGenre("TestGenre");
            GenreDto addedGenre = songifyCrudFacade.addGenre(genreRequest);
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            songifyCrudFacade.updateSongGenreById(addedSong.id(), new UpdateGenreDto(addedGenre.id()));
            /// Checking if song and genre was correctly added to db
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findGenreDtoById(addedGenre.id())).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(addedGenre.id(), genreRequest.name());
            // When
            SongGenreDto songGenreDtoById = songifyCrudFacade.findSongGenreDtoById(addedSong.id());
            // Then
            assertThat(songGenreDtoById).isNotNull().isExactlyInstanceOf(SongGenreDto.class);
            assertThat(songGenreDtoById).extracting(SongGenreDto::songId, SongGenreDto::genre)
                    .containsExactly(addedSong.id(), addedGenre);
        }
    }

    @Nested
    @DisplayName("FindAlbumByIdWithArtistsAndSongs - Tests")
    class FindAlbumByIdWithArtistsAndSongsTests {

        @Test
        @DisplayName("Should return AlbumNotFoundException When albumId was not found")
        void should_return_AlbumNotFoundException() {
            // Given
            // When
            Throwable albumException = catchThrowable(() -> songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(Long.MAX_VALUE));
            // Then
            assertThat(albumException).isInstanceOf(AlbumNotFoundException.class)
                    .hasMessage("Album with id: " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return Album When method was called")
        void should_return_Album_When_method_was_called() {
            // Given
            /// New album with song test song
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            AlbumDto addedAlbum = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            AlbumDtoWithArtistsAndSongsResponseDto albumByIdWithArtistsAndSongs =
                    songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedSong.id());
            // Then
            assertThat(albumByIdWithArtistsAndSongs).isNotNull()
                    .isExactlyInstanceOf(AlbumDtoWithArtistsAndSongsResponseDto.class);
            assertThat(albumByIdWithArtistsAndSongs)
                    .extracting(AlbumDtoWithArtistsAndSongsResponseDto::id, AlbumDtoWithArtistsAndSongsResponseDto::name)
                    .containsExactly(addedAlbum.id(), addedAlbum.title());
            assertThat(albumByIdWithArtistsAndSongs.songs().size()).isEqualTo(1);
            assertThat(albumByIdWithArtistsAndSongs.artists().size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("UpdatesSongPartiallyById - Tests")
    class UpdatesSongPartiallyByIdTests {

        @Test
        @DisplayName("Should throw SongNotFoundException When songId is not in db")
        void should_throwSongNotFoundException_When_songIdIsNotInDb() {
            // Given
            UpdateSongRequestDto updateRequest = UpdateSongRequestDto.builder()
                    .songName("TestSong")
                    .duration(321L)
                    .build();
            // When
            Throwable songException = catchThrowable(() ->
                    songifyCrudFacade.updatesSongPartiallyById(Long.MAX_VALUE, updateRequest));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return songDto When correct songId and UpdateSongRequestDto was sent")
        void should_return_songDto_When_songId_And_UpdateSongRequestDto_Was_Sent() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong"));
            UpdateSongRequestDto updateSongRequestDto = UpdateSongRequestDto.builder()
                    .songName("UpdatedTestSong")
                    .duration(321L)
                    .build();
            /// Check if the song was correctly added
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id())).extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(addedSong.id(), addedSong.name(), 123L);
            // When
            songifyCrudFacade.updatesSongPartiallyById(addedSong.id(), updateSongRequestDto);
            SongDto updatedSong = songifyCrudFacade.findSongDtoById(addedSong.id());
            // Then
            assertThat(updatedSong).isNotNull().isExactlyInstanceOf(SongDto.class);
            assertThat(updatedSong).extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(0L, "UpdatedTestSong", 321L);
        }
    }

    @Nested
    @DisplayName("DeleteSongById - Tests")
    class DeleteSongByIdTests {

        @Test
        @DisplayName("Should throw SongNotFoundException When incorrect songId was sent")
        void should_throw_SongNotFoundException_When_songId_Is_Not_In_Db() {
            // Given
            // When
            Throwable songException = catchThrowable(() -> songifyCrudFacade.deleteSongById(Long.MAX_VALUE));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should delete song from db When method with correct songId was called")
        void should_delete_song_from_db_When_correct_songId_Was_Sent() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            /// Check if song was correctly added
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id())).extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(addedSong.id(), addedSong.name(), addedSong.duration());
            // When
            songifyCrudFacade.deleteSongById(addedSong.id());
            // Then
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("DeleteAlbumById - Tests")
    class DeleteAlbumByIdTests {

        @Test
        @DisplayName("Should throw AlbumNotFoundException When albumId was not found")
        void should_throw_AlbumNotFoundException_When_albumId_Is_Not_In_Db() {
            // Given
            // When
            Throwable albumException = catchThrowable(() -> songifyCrudFacade.deleteAlbumById(Long.MAX_VALUE));
            // Then
            assertThat(albumException).isInstanceOf(AlbumNotFoundException.class)
                    .hasMessage("Album with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should throw AlbumNotEmptyException When method was called at album with song/s")
        void should_throw_AlbumNotEmptyException_When_album_with_song_was_called_in_method() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            /// New album with song test song
            AlbumDto addedAlbumWithSong = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbumWithSong.id()).songs().size()).isEqualTo(1);
            // When
            Throwable albumException = catchThrowable(() -> songifyCrudFacade.deleteAlbumById(addedAlbumWithSong.id()));
            // Then
            assertThat(albumException).isInstanceOf(AlbumNotEmptyException.class)
                    .hasMessage("Album is not empty, cannot delete");
        }

        @Test
        @DisplayName("Should delete empty Album When method was called with correct albumId")
        void should_delete_empty_Album_When_correct_albumId_Was_Sent() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            /// New album with song test song
            AlbumWithSongRequestDto albumWithSongRequestDto = TestEntityFactory.anAlbumWithSong(addedSong);
            AlbumDto addedAlbumWithSong = songifyCrudFacade.addAlbumWithSong(albumWithSongRequestDto);
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbumWithSong.id()).songs().size()).isEqualTo(1);
            /// Deleting Song from album
            songifyCrudFacade.deleteSongById(addedSong.id());
            // When
            songifyCrudFacade.deleteAlbumById(addedAlbumWithSong.id());
            // Then
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("DeleteGenreById - Tests")
    class DeleteGenreByIdTests {

        @Test
        @DisplayName("Should return GenreNotFoundException When genreId was not found")
        void should_return_GenreNotFoundException_When_genreId_Was_NotFound() {
            // Given
            // When
            Throwable genreException = catchThrowable(() -> songifyCrudFacade.deleteGenreById(Long.MAX_VALUE));
            // Then
            assertThat(genreException).isInstanceOf(GenreNotFoundException.class)
                    .hasMessage("Genre with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should throw GenreIsUsedBySongsException When genre with songs was given in method argument")
        void should_throw_GenreIsUsedBySongsException_When_genre_with_songs_was_given_in_method_argument() {
            // Given
            GenreRequestDto genreRequest = TestEntityFactory.aGenre();
            SongRequestDto songRequest = TestEntityFactory.aSong();
            GenreDto addedGenre = songifyCrudFacade.addGenre(genreRequest);
            SongDto addedSong = songifyCrudFacade.addSong(songRequest);
            songifyCrudFacade.updateSongGenreById(addedSong.id(), new UpdateGenreDto(addedGenre.id()));
            /// Checking if everything was correctly added
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(2);
            GenreWithSongsResponseDto genreDtoWithSongsDto = songifyCrudFacade.findGenreDtoWithSongsDto(addedGenre.id());
            assertThat(genreDtoWithSongsDto.songs().size()).isEqualTo(1);
            assertThat(genreDtoWithSongsDto.songs().iterator().next().name()).isEqualTo(addedSong.name());
            // When
            Throwable genreException = catchThrowable(() -> songifyCrudFacade.deleteGenreById(addedGenre.id()));
            // Then
            assertThat(genreException).isInstanceOf(GenreIsUsedBySongsException.class)
                    .hasMessage("Cannot delete genre, because it is used by songs.");

        }

        @Test
        @DisplayName("Should delete genre from db")
        void should_delete_genre_from_db() {
            // Given
            GenreRequestDto genreRequest = TestEntityFactory.aGenre();
            GenreDto addedGenre = songifyCrudFacade.addGenre(genreRequest);
            /// Check if genre was correctly added (2 in assertion, because we have "Default" Genre at id 1)
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(2);
            assertThat(songifyCrudFacade.findGenreDtoById(addedGenre.id())).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(addedGenre.id(), genreRequest.name());
            // When
            songifyCrudFacade.deleteGenreById(addedGenre.id());
            // Then
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("DeleteArtistByIdWithAlbumsAndSongs - Tests")
    class DeleteArtistByIdWithAlbumsAndSongsTests {

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
            assertThat(throwable).isExactlyInstanceOf(ArtistNotFoundException.class)
                    .hasMessage("Artist with id 0 not found.");
        }

        @Test
        @DisplayName("Should add and delete artist by id when he had no albums Then db size should be empty")
        public void should_add_and_delete_artist_by_id_when_he_had_no_albums_Then_db_size_should_be_empty() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            Set<AlbumDtoWithArtistsAndSongsResponseDto> albumsByArtistId =
                    songifyCrudFacade.findAlbumsByArtistId(addedArtist.id());
            assertThat(albumsByArtistId.size()).isEqualTo(0);
            // When
            songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(addedArtist.id());
            // Then
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should delete artist by id when he have one album with song Then db size should be empty")
        public void Should_delete_artist_by_id_when_he_have_one_album_with_song_Then_db_size_should_be_empty() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            Set<AlbumDtoWithArtistsAndSongsResponseDto> albumsByArtistId =
                    songifyCrudFacade.findAlbumsByArtistId(addedArtist.id());
            assertThat(albumsByArtistId.size()).isEqualTo(0);
            /// New song because we need it for album
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            /// New album with song test song
            AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            /// Adding album to artist and then checking is it's saved
            songifyCrudFacade.addArtistToAlbum(addedArtist.id(), albumDto.id());
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findSongDtoById(addedSong.id()).id()).isEqualTo(0L);
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumsByArtistId(albumDto.id()).size()).isEqualTo(1);
            // When
            songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(addedArtist.id());
            // Then
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(0);
            /// Check if albums and songs are correctly deleted from db after deleting artist **/
            Throwable songThrowable = catchThrowable(() -> songifyCrudFacade.findSongDtoById(0L));
            Throwable albumThrowable = catchThrowable(() -> songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(0L));
            assertThat(songifyCrudFacade.findAlbumsByArtistId(albumDto.id()).size()).isEqualTo(0);
            assertThat(songThrowable).isExactlyInstanceOf(SongNotFoundException.class);
            assertThat(albumThrowable).isExactlyInstanceOf(AlbumNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("AddArtistToAlbum - Tests")
    class AddArtistToAlbumTests {

        @Test
        @DisplayName("Should return ArtistNotFoundExcepion When Artist was not found")
        void should_return_artist_not_found_when_was_not_found() {
            // Given
            // When
            Throwable artistException = catchThrowable(() ->
                    songifyCrudFacade.addArtistToAlbum(Long.MAX_VALUE, Long.MAX_VALUE));
            // Then
            assertThat(artistException).isExactlyInstanceOf(ArtistNotFoundException.class)
                    .hasMessage("Artist with id " + Long.MAX_VALUE + " not found.");
        }

        @Test
        @DisplayName("Should return AlbumNotFoundException When Album was not found")
        void should_return_album_not_found_when_was_not_found() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            /// Checking if everything was correctly added
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            Throwable albumException = catchThrowable(() -> songifyCrudFacade.addArtistToAlbum(addedArtist.id(), Long.MAX_VALUE));
            // Then
            assertThat(albumException).isExactlyInstanceOf(AlbumNotFoundException.class)
                    .hasMessage("Album with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should add artist to album")
        void should_add_artist_to_album() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            AlbumDto addedAlbum = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            /// Checking if everything was correctly added
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbum.id()).songs().size()).isEqualTo(1);
            // When
            /// Void method, have to check by other methods
            songifyCrudFacade.addArtistToAlbum(addedArtist.id(), addedAlbum.id());
            // Then
            assertThat(songifyCrudFacade.findAlbumsByArtistId(addedArtist.id()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumsByArtistId(
                    addedAlbum.id()).iterator().next().artists().size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumsByArtistId(
                    addedAlbum.id()).iterator().next().name()).isEqualTo("TestAlbum");
        }


    }

    @Nested
    @DisplayName("UpdateArtistNameById - Tests")
    class updateArtistNameByIdTests {

        @Test
        @DisplayName("Should return ArtistNotFoundException")
        void should_return_artist_not_found_When_was_not_found() {
            // Given
            // When
            Throwable artistException = catchThrowable(
                    () -> songifyCrudFacade.updateArtistNameById(Long.MAX_VALUE, "TestArtist"));
            // Then
            assertThat(artistException).isExactlyInstanceOf(ArtistNotFoundException.class)
                    .hasMessage("Artist with id " + Long.MAX_VALUE + " not found.");
        }

        @Test
        @DisplayName("Should update Artist name")
        void should_update_artist_name() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findArtistById(addedArtist.id()).name()).isEqualTo("TestArtist");
            // When
            songifyCrudFacade.updateArtistNameById(addedArtist.id(), "UpdatedTestArtist");
            // Then
            assertThat(songifyCrudFacade.findArtistById(addedArtist.id()).name()).isEqualTo("UpdatedTestArtist");

        }
    }

    @Nested
    @DisplayName("FindArtistById - Tests")
    class findArtistByIdTests {

        @Test
        @DisplayName("Should return ArtistNotFoundException")
        void should_return_artist_not_found_When_was_not_found() {
            // Given
            // When
            Throwable artistException = catchThrowable(
                    () -> songifyCrudFacade.updateArtistNameById(Long.MAX_VALUE, "TestArtist"));
            // Then
            assertThat(artistException).isExactlyInstanceOf(ArtistNotFoundException.class)
                    .hasMessage("Artist with id " + Long.MAX_VALUE + " not found.");
        }

        @Test
        @DisplayName("Should return Artist")
        void should_return_artist() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            // When
            ArtistDto returnedArtist = songifyCrudFacade.findArtistById(addedArtist.id());
            // Then
            assertThat(returnedArtist).isEqualTo(addedArtist);
        }
    }

    @Nested
    @DisplayName("UpdateGenreNameById - Tests")
    class updateGenreNameByIdTests {

        @Test
        @DisplayName("Should return GenreNotFoundException")
        void should_return_GenreNotFoundException_When_Genre_was_not_found() {
            // Given
            // When
            Throwable genreNotFoundException = catchThrowable(
                    () -> songifyCrudFacade.updateGenreNameById(Long.MAX_VALUE, "UpdatedGenreName"));
            // Then
            assertThat(genreNotFoundException).isExactlyInstanceOf(GenreNotFoundException.class)
                    .hasMessage("Genre with id " + Long.MAX_VALUE + " not found.");
        }

        @Test
        @DisplayName("Should update name of default genre with id 1L")
        void should_update_name_of_default_genre_with_id_1() {
            // Given
            /// Checking if default is correctly added
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findGenreDtoById(1L).name()).isEqualTo("Default");
            // When
            songifyCrudFacade.updateGenreNameById(1L, "Updated_Default");
            // Then
            assertThat(songifyCrudFacade.findGenreDtoById(1L).name()).isEqualTo("Updated_Default");
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("UpdateAlbumByIdWithSongsAndArtists - Tests")
    class updateAlbumByIdWithSongsAndArtistsTests {

        @Test
        @DisplayName("Should throw AlbumNotFoundException")
        void should_throw_AlbumNotFoundException_When_Album_was_not_found() {
            // Given
            UpdateAlbumWithSongsAndArtistsRequestDto albumRequest = UpdateAlbumWithSongsAndArtistsRequestDto.builder()
                    .newName("TestAlbum")
                    .songIds(new ArrayList<>())
                    .artistIds(new ArrayList<>())
                    .build();
            // When
            Throwable albumNotFoundException = catchThrowable(
                    () -> songifyCrudFacade.updateAlbumByIdWithSongsAndArtists(Long.MAX_VALUE, albumRequest));
            // Then
            assertThat(albumNotFoundException).isExactlyInstanceOf(AlbumNotFoundException.class)
                    .hasMessage("Album with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should add list of songs to existing album")
        void should_add_list_of_songs_to_existing_album() {
            // Given
            SongDto addedSong_1 = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong_1"));
            AlbumDto addedAlbum = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong_1));
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbum.id()).name())
                    .isEqualTo("TestAlbum");
            List<SongDto> listOfSongs = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbum.id()).songs().stream()
                    .map(songInfoDto -> new SongDto(songInfoDto.id(), songInfoDto.name(), songInfoDto.duration()))
                    .toList();
            assertThat(listOfSongs.contains(addedSong_1)).isTrue();
            assertThat(listOfSongs.size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbum.id()).songs()
                    .size()).isEqualTo(1);
            SongDto addedSong_2 = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong_2"));
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(2);

            ArrayList listOfSongsIdsToAdd = new ArrayList();
            listOfSongsIdsToAdd.add(addedSong_2.id());

            UpdateAlbumWithSongsAndArtistsRequestDto updateAlbumRequest = UpdateAlbumWithSongsAndArtistsRequestDto.builder()
                    .newName("UpdatedTestAlbum")
                    .artistIds(new ArrayList<>())
                    .songIds(listOfSongsIdsToAdd)
                    .build();

            // When
            UpdateAlbumWithSongsAndArtistsResponseDto updatedTestAlbum = songifyCrudFacade.
                    updateAlbumByIdWithSongsAndArtists(addedAlbum.id(), updateAlbumRequest);
            // Then
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(2);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(updatedTestAlbum.title()).isEqualTo("UpdatedTestAlbum");
            assertThat(updatedTestAlbum.songs().size()).isEqualTo(2);
            assertThat(updatedTestAlbum.songs().contains(addedSong_1)).isTrue();
            assertThat(updatedTestAlbum.songs().contains(addedSong_2)).isTrue();
        }

        @Test
        @DisplayName("Should add Artist to album")
        void should_add_artist_to_album() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            AlbumDto addedAlbum = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            ArrayList listOfArtistsIdsToAdd = new ArrayList();
            listOfArtistsIdsToAdd.add(addedArtist.id());
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbum.id()).name()).isEqualTo("TestAlbum");
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedSong.id()).songs()
                    .iterator().next().name()).isEqualTo("TestSong");
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedSong.id()).songs()
                    .size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedSong.id()).artists()
                    .size()).isEqualTo(0);
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);

            UpdateAlbumWithSongsAndArtistsRequestDto updateAlbumRequest = UpdateAlbumWithSongsAndArtistsRequestDto.builder()
                    .newName("UpdatedTestAlbum")
                    .artistIds(listOfArtistsIdsToAdd)
                    .songIds(new ArrayList<>())
                    .build();
            // When
            UpdateAlbumWithSongsAndArtistsResponseDto updatedTestAlbum = songifyCrudFacade.
                    updateAlbumByIdWithSongsAndArtists(addedAlbum.id(), updateAlbumRequest);


            // Then
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(updatedTestAlbum.title()).isEqualTo("UpdatedTestAlbum");
            assertThat(updatedTestAlbum.songs().size()).isEqualTo(1);
            assertThat(updatedTestAlbum.songs().iterator().next()).extracting(SongDto::name).isEqualTo("TestSong");
            assertThat(updatedTestAlbum.artists().size()).isEqualTo(1);
            assertThat(updatedTestAlbum.artists().iterator().next()).extracting(ArtistDto::name).isEqualTo("TestArtist");


        }
    }

    @Nested
    @DisplayName("UpdateSongAlbum - Tests")
    class updateSongAlbumTests {

        @Test
        @DisplayName("Should return AlbumNotFoundException")
        void should_return_AlbumNotFoundException() {
            // Given
            UpdateSongAlbumRequestDto updateSongAlbumRequest = UpdateSongAlbumRequestDto.builder()
                    .albumId(Long.MAX_VALUE)
                    .build();
            // When
            Throwable songException = catchThrowable(() ->
                    songifyCrudFacade.updateSongAlbum(Long.MAX_VALUE, updateSongAlbumRequest));
            // Then
            assertThat(songException).isInstanceOf(AlbumNotFoundException.class)
                    .hasMessage("Album with id " + Long.MAX_VALUE + " not found.");
        }

        @Test
        @DisplayName("Should return SongNotFoundException")
        void should_return_SongNotFoundException() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            AlbumDto addedAlbumWithSong = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));

            UpdateSongAlbumRequestDto updateSongAlbumRequest = UpdateSongAlbumRequestDto.builder()
                    .albumId(addedAlbumWithSong.id())
                    .build();
            // When
            Throwable songException = catchThrowable(() ->
                    songifyCrudFacade.updateSongAlbum(Long.MAX_VALUE, updateSongAlbumRequest));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should add Song to Album")
        void should_add_song_to_album() {
            // Given
            SongDto addedSong_1 = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong_1"));
            SongDto addedSong_2 = songifyCrudFacade.addSong(TestEntityFactory.aSong("TestSong_2"));
            AlbumDto addedAlbumWithSong = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong_1));

            UpdateSongAlbumRequestDto updateSongAlbumRequest = UpdateSongAlbumRequestDto.builder()
                    .albumId(addedAlbumWithSong.id())
                    .build();
            /// Check if everything added corretly
            assertThat(addedAlbumWithSong.title()).isEqualTo("TestAlbum");
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(2);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbumWithSong.id()).songs().size()).isEqualTo(1);
            // When
            UpdateSongAlbumResponseDto updatedTestAlbum = songifyCrudFacade.updateSongAlbum(addedSong_2.id(), updateSongAlbumRequest);
            // Then
            assertThat(updatedTestAlbum.message()).isEqualTo(
                    "Successfully added song with id: " + addedSong_2.id()
                            + " to album with id: " + addedAlbumWithSong.id() + ".");
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(addedAlbumWithSong.id()).songs().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("UpdateSongGenreById - Tests")
    class updateSongGenreByIdTests {

        @Test
        @DisplayName("Should throw SongNotFoundException")
        void should_throw_SongNotFoundException() {
            // Given
            // When
            Throwable songException = catchThrowable(() ->
                    songifyCrudFacade.updateSongGenreById(Long.MAX_VALUE, new UpdateGenreDto(0L)));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should throw GenreNotFoundException")
        void should_throw_GenreNotFoundException() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            Throwable genreException = catchThrowable(() ->
                    songifyCrudFacade.updateSongGenreById(addedSong.id(), new UpdateGenreDto(0L)));
            // Then
            assertThat(genreException).isInstanceOf(GenreNotFoundException.class)
                    .hasMessage("Genre with id " + addedSong.id() + " not found");
        }

        @Test
        @DisplayName("Should set new Genre to song")
        void should_set_new_genre() {
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            GenreDto addedGenre = songifyCrudFacade.addGenre(TestEntityFactory.aGenre());
            UpdateGenreDto updateDto = new UpdateGenreDto(addedGenre.id());
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(2);
            SongGenreDto songGenreDto = songifyCrudFacade.findSongGenreDtoById(addedSong.id());
            assertThat(songGenreDto.genre().name())
                    .isNotEqualTo(addedGenre.name())
                    .isEqualTo("Default");
            // When
            songifyCrudFacade.updateSongGenreById(addedSong.id(), updateDto);
            // Then

            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(2);
            SongGenreDto updatedSongGenreDto = songifyCrudFacade.findSongGenreDtoById(addedSong.id());
            assertThat(updatedSongGenreDto.genre().name())
                    .isEqualTo(addedGenre.name());
        }
    }

    @Nested
    @DisplayName("FindGenreDtoById - Tests")
    class findGenreDtoByIdTests {

        @Test
        @DisplayName("Should throw GenreNotFoundException")
        void should_throw_GenreNotFoundException() {
            // Given
            // When
            Throwable genreException = catchThrowable(() -> songifyCrudFacade.findGenreDtoById(Long.MAX_VALUE));
            // Then
            assertThat(genreException).isInstanceOf(GenreNotFoundException.class)
                    .hasMessage("Genre with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should find genreDto When correct Genre Id was given")
        void should_find_genreDto_When_correct_GenreId() {
            // Given
            GenreDto addedGenre = songifyCrudFacade.addGenre(TestEntityFactory.aGenre());
            /// Check if everything is added correctly
            /// "...isEqualTo = 2" because we have "Default" Genre at ID = 1
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(2);
            // When
            GenreDto fetchedGenreDto = songifyCrudFacade.findGenreDtoById(addedGenre.id());
            // Then
            assertThat(fetchedGenreDto).extracting(GenreDto::id, GenreDto::name).
                    containsExactly(addedGenre.id(), addedGenre.name());
        }
    }

    @Nested
    @DisplayName("FindAllGenreDto - Tests")
    class findAllGenreDtoTests {
        @Test
        @DisplayName("Should return list of all Genres with only default Genre with id 1")
        void should_return_empty_list_of_genre() {
            // Given
            // When
            List<GenreDto> listOfAllGenres = songifyCrudFacade.findAllGenreDto(Pageable.unpaged());
            // Then
            assertThat(listOfAllGenres.size()).isEqualTo(1);
            assertThat(listOfAllGenres.iterator().next()).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(1L, "Default");
        }
    }

    @Nested
    @DisplayName("FindAllAlbumDto - Tests")
    class findAllAlbumDtoTests {

        @Test
        @DisplayName("Should return empty List")
        void should_return_empty_list_of_albums() {
            // Given
            // When
            List<AlbumDto> allAlbumDtoList = songifyCrudFacade.findAllAlbumDto(Pageable.unpaged());
            // Then
            assertThat(allAlbumDtoList).isInstanceOf(List.class);
            assertThat(allAlbumDtoList.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("FindGenreDtoWithSongsDto - Tests")
    class findGenreDtoWithSongsDtoTests {

        @Test
        @DisplayName("Should return GenreNotFoundException")
        void should_throw_GenreNotFoundException() {
            // Given
            // When
            Throwable genreException = catchThrowable(() ->
                    songifyCrudFacade.findGenreDtoWithSongsDto(Long.MAX_VALUE));
            // Then
            assertThat(genreException).isInstanceOf(GenreNotFoundException.class)
                    .hasMessage("Genre with id " + Long.MAX_VALUE + " not found");
        }

        @Test
        @DisplayName("Should return genreDto with List<SongDto>")
        void should_return_genreDto_with_list_of_songs() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            GenreDto addedGenre = songifyCrudFacade.addGenre(TestEntityFactory.aGenre());
            songifyCrudFacade.updateSongGenreById(addedSong.id(), new UpdateGenreDto(addedGenre.id()));
            /// Check if everything is added correctly
            /// Genres equals = 2, because of default genre in db on id 1L
            assertThat(songifyCrudFacade.findAllGenreDto(Pageable.unpaged()).size()).isEqualTo(2);
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            // When
            GenreWithSongsResponseDto fetchedGenreDtoWithSongsDtoList =
                    songifyCrudFacade.findGenreDtoWithSongsDto(addedGenre.id());
            // Then
            assertThat(fetchedGenreDtoWithSongsDtoList.genreDto()).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(addedGenre.id(), addedGenre.name());
            assertThat(fetchedGenreDtoWithSongsDtoList.songs().size()).isEqualTo(1);
            assertThat(fetchedGenreDtoWithSongsDtoList.songs().iterator().next()).extracting(SongDto::id, SongDto::name)
                    .containsExactly(addedSong.id(), addedSong.name());
        }
    }

    @Nested
    @DisplayName("FindArtistDtoWithAlbumsDto - Tests")
    class findArtistDtoWithAlbumsDtoTests {

        @Test
        @DisplayName("Should return ArtistNotFoundException")
        void should_throw_ArtistNotFoundException() {
            // Given
            // When
            Throwable artistException = catchThrowable(() ->
                    songifyCrudFacade.findArtistDtoWithAlbumsDto(Long.MAX_VALUE));
            // Then
            assertThat(artistException).isInstanceOf(ArtistNotFoundException.class)
                    .hasMessage("Artist with id " + Long.MAX_VALUE + " not found.");

        }

        @Test
        @DisplayName("Should return Artist with List of Album")
        void should_return_artist_with_list_of_album() {
            // Given
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            AlbumDto addedAlbum = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            /// Checking if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            songifyCrudFacade.addArtistToAlbum(addedArtist.id(), addedAlbum.id());
            // When
            ArtistWithAlbumsResponseDto fetchedArtistWithAlbums = songifyCrudFacade.findArtistDtoWithAlbumsDto(addedArtist.id());
            // Then
            assertThat(fetchedArtistWithAlbums.artistDto()).extracting(ArtistDto::id, ArtistDto::name)
                    .containsExactly(addedArtist.id(), addedArtist.name());
            assertThat(fetchedArtistWithAlbums.allAlbumsResponseDto().allAlbums().iterator().next())
                    .extracting(AlbumDto::id, AlbumDto::title).containsExactly(addedAlbum.id(), addedAlbum.title());
        }

    }

    @Nested
    @DisplayName("FindAlbumsByArtistId - Tests")
    class findAlbumsByArtistIdTests {

        @Test
        @DisplayName("Should return empty Set")
        void should_return_empty_set() {
            // Given
            // When
            Set<AlbumDtoWithArtistsAndSongsResponseDto> albumsByArtistId =
                    songifyCrudFacade.findAlbumsByArtistId(Long.MAX_VALUE);
            // Then
            assertThat(albumsByArtistId.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return Set of Albums pinned to given ArtistId")
        void should_return_set_of_albums_pinned_to_given_artist_id() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            AlbumDto addedAlbum = songifyCrudFacade.addAlbumWithSong(TestEntityFactory.anAlbumWithSong(addedSong));
            ArtistDto addedArtist = songifyCrudFacade.addArtist(TestEntityFactory.anArtist());
            songifyCrudFacade.addArtistToAlbum(addedArtist.id(), addedAlbum.id());
            /// Check if everything is added correctly
            assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findAllAlbumDto(Pageable.unpaged()).size()).isEqualTo(1);
            assertThat(songifyCrudFacade.findArtistDtoWithAlbumsDto(addedArtist.id()).allAlbumsResponseDto().allAlbums().iterator().next())
                    .extracting(AlbumDto::id, AlbumDto::title)
                    .containsExactly(addedAlbum.id(), addedAlbum.title());
            // When
            Set<AlbumDtoWithArtistsAndSongsResponseDto> fetchedArtistWithAlbums =
                    songifyCrudFacade.findAlbumsByArtistId(addedArtist.id());
            // Then
            assertThat(fetchedArtistWithAlbums.size()).isEqualTo(1);
            assertThat(fetchedArtistWithAlbums.iterator().next()).extracting(AlbumDtoWithArtistsAndSongsResponseDto::id,
                    AlbumDtoWithArtistsAndSongsResponseDto::name).containsExactly(addedAlbum.id(), addedAlbum.title());
            assertThat(fetchedArtistWithAlbums.iterator().next().songs().iterator().next()).extracting(SongInfoDto::id, SongInfoDto::name)
                    .containsExactly(addedSong.id(), addedSong.name());
            assertThat(fetchedArtistWithAlbums.iterator().next().artists().iterator().next()).extracting(ArtistDto::id, ArtistDto::name)
                    .containsExactly(addedArtist.id(), addedArtist.name());
        }
    }

    @Nested
    @DisplayName("FindSongDtoWithGenreDtoById - Tests")
    class findSongDtoWithGenreDtoById {

        @Test
        @DisplayName("Should return SongNotFoundException")
        void should_return_songNotFoundException() {
            // Given
            // When
            Throwable songException = catchThrowable(() -> songifyCrudFacade.findSongDtoWithGenreDtoById(Long.MAX_VALUE));
            // Then
            assertThat(songException).isInstanceOf(SongNotFoundException.class)
                    .hasMessage("Song with id: " + Long.MAX_VALUE + " not found.");
        }

        @Test
        @DisplayName("Should return SongDto with Default GenreDto")
        void should_return_songDto_with_default_genre() {
            // Given
            SongDto addedSong = songifyCrudFacade.addSong(TestEntityFactory.aSong());
            GenreDto defaultGenre = songifyCrudFacade.findGenreDtoById(1L);
            // When
            SongWithGenreResponseDto responseDto = songifyCrudFacade.findSongDtoWithGenreDtoById(addedSong.id());
            // Then
            assertThat(responseDto.song()).extracting(SongDto::id, SongDto::name, SongDto::duration)
                    .containsExactly(addedSong.id(), addedSong.name(), addedSong.duration());
            assertThat(responseDto.genre()).extracting(GenreDto::id, GenreDto::name)
                    .containsExactly(defaultGenre.id(), defaultGenre.name());
        }
    }
}