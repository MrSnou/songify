package com.songify.infrastructure.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.songify.domain.security.jwt.JwtTokenValidator;
import com.songify.domain.usercrud.User;
import com.songify.domain.usercrud.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
class AdminCheckController {

    private final UserRepository userRepository;
    private final JwtTokenValidator jwtTokenValidator;

    @PatchMapping("/setAdminUser")
    public ResponseEntity<MessageDto> setAdminUser(Authentication authentication, @RequestParam String adminPassword) {

        if (adminPassword.equals("SuperHardAdminPassword1234!")) {
            Object principal = authentication.getPrincipal();
            String email;

            if (principal instanceof SecurityUser user) {
                email = user.getUsername();

            } else if (principal instanceof OAuth2User oauthUser) {
                email = oauthUser.getAttribute("email");

            } else {
                throw new RuntimeException("Unknown principal type: " + principal.getClass());
            }

            User fetchedUser = userRepository.findFirstByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + " not found."));

            fetchedUser.getAuthorities().add("ROLE_ADMIN");
            userRepository.save(fetchedUser);

            List<String> authorities = fetchedUser.getAuthorities().stream()
                    .map(authority -> authority.toString())
                    .toList();

            MessageDto message = new MessageDto(
                    "Hi " + email + ", you have been granted admin privileges.", authorities, null);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else  {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/userDetails")
    public ResponseEntity<MessageDto> message(final HttpServletRequest request,
                                              final HttpServletResponse response, Authentication authentication) {
        if (request.getCookies() == null) ResponseEntity.ok(new MessageDto("Hello, you should not be here!", null, null));
        String token = Arrays.stream(request.getCookies())
                .map(cookie ->
                        {
                            if (cookie.getName().equals("AuthorizationToken")) return cookie.getValue();
                            return null;
                        }
                ).filter(value -> value != null)
                .findFirst().orElse(null);
        if (token != null) {
            DecodedJWT decodedToken = jwtTokenValidator.validate(token);
            String email = decodedToken.getSubject();
            List<String> roles = decodedToken.getClaim("roles").asList(String.class);
            return ResponseEntity.ok(new MessageDto("Hello " + email, roles, token));
        }
        return ResponseEntity.ok(new MessageDto("Hello, you should not be here!", null, null));
    }


    private record MessageDto(String message, List<String> authorities, String token) { }
}
