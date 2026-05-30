package feature;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BasicSecurityMeasuresIntegrationTest extends BaseIntegrationTest {

    @Value("${jwt.key.private}")
    private String privateKey;
    @Value("${jwt.key.public}")
    private String publicKey;
    @Autowired
    private RSAPrivateKey rsaPrivateKey;

    @Nested
    @DisplayName("Login/Logout and registration tests")
    class JWT_Login_Logout_Tests {

        @Test
        @DisplayName("Should register user and return 201 with message.")
        void shouldRegisterUserAndReturn201() throws Exception {
            // Given
            // When
            ResultActions registerResult = mockMvc.perform(post("/users/register")
                    .content("""
                            {
                                "email": "TestUser@Test.test",
                                "password": "TestPassword"
                            }
                            """)
                    .contentType(MediaType.APPLICATION_JSON));
            // Then
            String contentAsString = registerResult.andReturn().getResponse().getContentAsString();
            assertThat(contentAsString).contains("User TestUser@Test.test registered successfully!");
            registerResult.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should login user, give him JWT token cookie and return status 200")
        void shouldLoginUserAndReturn200() throws Exception {
            /// 0. Login user with default test user data and save cookie with JWT token.
            // Given
            // When
                ResultActions loginResult = mockMvc.perform(post("/login")
                        .content("""
                                {
                                    "email": "Admin",
                                    "password": "admin"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON));
            // Then
            loginResult.andExpect(status().isOk());
            /// 1. Check if token is correct
            // Given
            Cookie authCookie = loginResult.andReturn().getResponse().getCookie("AuthorizationToken");
            Assertions.assertNotNull(authCookie);
            // When
            ResultActions getSongResult = mockMvc.perform(get("/songs").cookie(authCookie));
            // Then
            getSongResult.andExpect(status().isOk())
                    .andExpect(result -> {
                String responseContent = result.getResponse().getContentAsString();
                Assertions.assertTrue(responseContent.contains("songs"));
            }).andExpect(jsonPath("$.songs").isArray());

        }
    }

    @Nested
    @DisplayName("Authorization protection tests")
    class Auth_Protection_Tests {

        @Test
        @DisplayName("Should return status 401 when unauthorized user tries to use functionality that requires authentication")
        void shouldReturnStatus401WhenUnauthorizedUserTriesToUseFunctionalityThatRequiresAuthentication() throws Exception {
            // Given
            // When
            ResultActions resultActions = mockMvc.perform(get("/api/songs"));
            // Then
            resultActions.andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 403 when user with insufficient permissions tries to use functionality that requires higher permissions")
        void shouldReturn403WhenUserWithInsufficientPermissionsTriesToUseFunctionalityThatRequiresHigherPermissions() throws Exception {
            ///  0. Login user
            // Given
            // When
            ResultActions loginResult = mockMvc.perform(post("/login")
                    .content("""
                            {
                                "email": "User",
                                "password": "user"
                            }
                            """)
                    .contentType(MediaType.APPLICATION_JSON));
            // Then
            loginResult.andExpect(status().isOk());
            /// 1. Try to use admin functionality with user credentials and token
            // given
            Cookie authCookie = loginResult.andReturn().getResponse().getCookie("AuthorizationToken");
            // When
            mockMvc.perform(post("/songs").cookie(authCookie)
                    .content("""
                            {
                                "name": "Test_Song",
                                "duration": 200L,
                                "releaseDate": "2024-01-01T00:00:00Z",
                                "language": "English"
                            }
                            """)
                    .contentType(MediaType.APPLICATION_JSON))
                    // Then
                    .andExpect(status().isForbidden());
        }



        @Test
        @DisplayName("Should return 401 when token is expired")
        void shouldReturn401WhenTokenIsExpired() throws Exception {
            // Given
            String expiredToken = JWT.create()
                    .withSubject("Admin")
                    .withClaim("roles", List.of("ROLE_USER", "ROLE_ADMIN"))
                    .withExpiresAt(Instant.now().minusSeconds(60))
                    .sign(Algorithm.RSA256(null, rsaPrivateKey));

            Cookie expiredCookie = new Cookie("AuthorizationToken", expiredToken);

            // When + Then
            mockMvc.perform(get("/songs").cookie(expiredCookie))
                    .andExpect(status().isUnauthorized());
        }

    }


}
