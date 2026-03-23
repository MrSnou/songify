package feature;


import com.songify.SongifyApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SongifyApplication.class)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class HappyPathIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    public MockMvc mockMvc;

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        System.out.println("PostgreSQL URL: " + postgreSQLContainer.getJdbcUrl());
    }

    @Nested
    @DisplayName("HappyPath Integration Test")
    class happyPathIntegrationTest {

        @Test
        @DisplayName("First Positive HappyPath Test")
        void firstPositiveHappyPathTest() throws Exception {
            /// 1. When I go to /songs, then I can see no songs.
            ResultActions getSongsResult = mockMvc.perform(get("/songs")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.songs", empty()));
            /// 2. When I post to /songs/ with song "Till I collapse",
            ///    then I can see that posted song is returned with id 1.
            mockMvc.perform(post("/songs")
                    .content("""
                            {
                                "name": "Till I Collapse",
                                "duration": 123,
                                "releaseDate": "2026-03-23T17:36:06.322Z",
                                "language": "ENGLISH"
                            }
                            """)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.song.id", is(1)))
                    .andExpect(jsonPath("$.song.name", is("Till I Collapse")))
                    .andExpect(jsonPath("$.song.duration", is(123)))
                    .andExpect(jsonPath("$.song.genre.id", is(1)))
                    .andExpect(jsonPath("$.song.genre.name", is("Default")));
            /// 3. When I post to /songs with song "Lose Yourself",
            ///    then I can see that posted song is returned with id 2.
            mockMvc.perform(post("/songs")
                            .content("""
                            {
                                "name": "Lose Yourself",
                                "duration": 321,
                                "releaseDate": "2026-03-23T17:36:06.322Z",
                                "language": "ENGLISH"
                            }
                            """)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                    ).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.song.id", is(2)))
                    .andExpect(jsonPath("$.song.name", is("Lose Yourself")))
                    .andExpect(jsonPath("$.song.duration", is(321)))
                    .andExpect(jsonPath("$.song.genre.id", is(1)))
                    .andExpect(jsonPath("$.song.genre.name", is("Default")));
            /// 4. When I go to /genres,
            ///    then I can see only "Default" Genre with id 1.

        }
    }
}




// 5. When I post to LH:8082/api/v1/genres/(Http_PostMethod with body) with Genre "Rap",
//      then I can see added Genre with id 2.
// 6. When I go to LH:8082/api/v1/genre/1,
//      then I can see Default genre with id 1.
// 7. When I update to (Http_PostMethod with PathVariable songId and UpdateGenreDto body)
//      LH:8082/api/v1/updateSongGenre/1 and UpdateGenreBody(genreId - 2), then I can see that Genre was added to song with id 1.
// 8. When I go to LH:8082/api/v1/songs/1,
//      then I can see song with "Rap" Genre.
// 9. When I go to LH:8082/api/v1/albums/,
//      then I can see no albums.
// 10. When I post to LH:8082/api/v1/albums/(Http_PostMethod with body) with album "Eminem_Album_1" and song id 1,
//      then album "Eminem_Album_1" is returned with id 1.
// 11. When I go to LH:8082/api/v1/albums/1,
//      then I can see no albums because there is no artists in the system.
// 12. When I post to LH:8082/api/v1/artists/(Http_PostMethod with body) with Artist "Eminem",
//      then artist "Eminem" is returned with id 1.
// 13. When I put to LH:8082/api/v1/artists/addArtistToAlbum/{artistId - 1}/{albumId - 1},
//      then I can see that Artist with id 1 was added to Album with id 1.
// 14. When I go to LH:8082/api/v1/albums/1,
//      then I can see album with single song with id 1, and artist with id 1.
// 15. When I put to LH:8082/api/v1/albums/(Http_PostMethod with body) with album id 1 and song id 2 ("Lose Yourself"),
//      then Song with id 2 us added to Album with id 1 ("Eminem_Album_1").
// 16. When I go to Lh:8082/api/v1/albums/1,
//      then I can see album with 2 songs (songId 1, songId 2) and one artist (artistId 1).
