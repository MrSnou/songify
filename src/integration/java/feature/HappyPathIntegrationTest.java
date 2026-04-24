package feature;


import com.songify.SongifyApplication;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
                            .with(jwt().authorities(() -> "ROLE_ADMIN"))
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
                            .with(jwt().authorities(() -> "ROLE_ADMIN"))
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
                            .with(jwt().authorities(() -> "ROLE_ADMIN"))
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
            mockMvc.perform(get("/songs/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.genre.id", is(1)))
                    .andExpect(jsonPath("$.genre.name", is("Default")));
            /// 7. When I put to /songs/1/genres,
            ///    then I can see that song with id 1 is with genre id 2.
            mockMvc.perform(patch("/songs/1/genres/2")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Successfully updated song genre with id: 1 to Rap.")))
                    .andExpect(jsonPath("$.updatedSong.id", is(1)))
                    .andExpect(jsonPath("$.updatedSong.name", is("Till I Collapse")))
                    .andExpect(jsonPath("$.updatedSong.duration", is(123)));
            /// 8. When I go to /songs/1,
            ///    then I can see song with "Rap" Genre.
            mockMvc.perform(get("/songs/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Till I Collapse")))
                    .andExpect(jsonPath("$.duration", is(123)))
                    .andExpect(jsonPath("$.genre.id", is(2)))
                    .andExpect(jsonPath("$.genre.name", is("Rap")));
            /// 9. When I go to /albums,
            ///    then I can see no albums.
            mockMvc.perform(get("/albums")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.albums", empty()));
            /// 10. When I post to /albums with album "Eminem_Album_1" and song id 1,
            ///     then album "Eminem_Album_1" is returned with id 1.
            mockMvc.perform(post("/albums")
                            .with(jwt().authorities(() -> "ROLE_ADMIN"))
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
            ///     then I can see album with id 1 with songs and no artists.
            mockMvc.perform(get("/albums/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Eminem_Album_1")))
                    .andExpect(jsonPath("$.songs", hasSize(1)))
                    .andExpect(jsonPath("$.artists", empty()));
            /// 12. When I post to /artists with Artist "Eminem",
            ///     then artist "Eminem" is returned with id 1.
            mockMvc.perform(post("/artists")
                            .with(jwt().authorities(() -> "ROLE_ADMIN"))
                    .content("""
                            {
                            "name": "Eminem"
                            }
                    """)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Eminem")));
            /// 13. When I put to /{artistId}/albums/{albumId},
            ///      then I can see that Artist with id 1 was added to Album with id 1.
            mockMvc.perform(patch("/artists/1/albums/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message",
                            is("Successfully added artist [1] Eminem to album [1] Eminem_Album_1.")));
            /// 14. When I go to /albums/1,
            ///     then I can see album with single song with id 1, and artist with id 1.
            mockMvc.perform(get("/albums/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Eminem_Album_1")))
                    .andExpect(jsonPath("$.songs", hasSize(1)))
                    .andExpect(jsonPath("$.artists", hasSize(1)));
            /// 15. When I patch to /songs/{songId}/albums/{albumId} with song id 2 and album id 1,
            ///     then Song with id 2 ("Lose Yourself") is added to Album with id 1 ("Eminem_Album_1").
            mockMvc.perform(patch("/songs/2/albums/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Successfully added song with id: 2 to album with id: 1.")))
                    .andExpect(jsonPath("$.albumDto.title", is("Eminem_Album_1")))
                    .andExpect(jsonPath("$.albumDto.artists", hasSize(1)))
                    .andExpect(jsonPath("$.albumDto.songs", hasSize(2)));
            /// 16. When I go to /albums/1,
            ///     then I can see album with 2 songs (songId 1, songId 2) and one artist (artistId 1).
            mockMvc.perform(get("/albums/1")
                            .with(jwt().authorities(() -> "ROLE_ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Eminem_Album_1")))
                    .andExpect(jsonPath("$.songs", hasSize(2)))
                    .andExpect(jsonPath("$.songs[*].id", containsInAnyOrder(1, 2)))
                    .andExpect(jsonPath("$.songs[*].name", containsInAnyOrder("Lose Yourself", "Till I Collapse")))
                    .andExpect(jsonPath("$.artists", hasSize(1)))
                    .andExpect(jsonPath("$.artists[0].id", is(1)))
                    .andExpect(jsonPath("$.artists[0].name", is("Eminem")));
        }
    }
}