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

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
                    .contentType(MediaType.APPLICATION_JSON)
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
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.song.id", is(2)))
                    .andExpect(jsonPath("$.song.name", is("Lose Yourself")))
                    .andExpect(jsonPath("$.song.duration", is(321)))
                    .andExpect(jsonPath("$.song.genre.id", is(1)))
                    .andExpect(jsonPath("$.song.genre.name", is("Default")));
            /// 4. When I go to /genres,
            ///    then I can see only "Default" Genre with id 1.
            mockMvc.perform(get("/genres"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.genres[0].id", is(1)))
                    .andExpect(jsonPath("$.genres[0].name", is("Default")));
            /// 5. When I post to /genres with Genre "Rap",
            ///    then I can see added Genre with id 2.
            mockMvc.perform(post("/genres")
                            .content("""
                                    {
                                        "name": "Rap"
                                    }
                                    """)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(2)))
                    .andExpect(jsonPath("$.name", is("Rap")));
            /// 6. When I go to /songs/1,
            ///    then I can see song with Default genre with id 1.
            mockMvc.perform(get("/songs/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.genre.id", is(1)))
                    .andExpect(jsonPath("$.genre.name", is("Default")));
            /// 7. When I put to /songs/1/genres,
            ///    then I can see that song with id 1 is with genre id 2.
            mockMvc.perform(patch("/songs/1/genres/2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Successfully updated song genre with id: 1 to Rap.")))
                    .andExpect(jsonPath("$.updatedSong.id", is(1)))
                    .andExpect(jsonPath("$.updatedSong.name", is("Till I Collapse")))
                    .andExpect(jsonPath("$.updatedSong.duration", is(123)));
            /// 8. When I go to /songs/1,
            ///    then I can see song with "Rap" Genre.
            mockMvc.perform(get("/songs/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Till I Collapse")))
                    .andExpect(jsonPath("$.duration", is(123)))
                    .andExpect(jsonPath("$.genre.id", is(2)))
                    .andExpect(jsonPath("$.genre.name", is("Rap")));
            /// 9. When I go to /albums,
            ///    then I can see no albums.
            mockMvc.perform(get("/albums"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.albums", empty()));
            /// 10. When I post to /albums with album "Eminem_Album_1" and song id 1,
            ///     then album "Eminem_Album_1" is returned with id 1.
            mockMvc.perform(post("/albums")
                    .content("""
                            {
                                "title": "Eminem_Album_1",
                                "releaseDate": "2026-03-28T10:24:29.905Z",
                                "songIds": [
                                1
                                ]
                            }
                            """)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.title", is("Eminem_Album_1")))
                    .andExpect(jsonPath("$.songIds", containsInAnyOrder(1)));
            /// 11. When I go to /albums,
            ///     then I can see no albums because there is no artists in the system.



        }
    }
}











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
