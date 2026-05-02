package com.songify.infrastructure.security;

import com.songify.domain.usercrud.User;
import com.songify.domain.usercrud.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class AdminCheckController {

    private final UserRepository userRepository;

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
                    "Hi " + email + ", you have been granted admin privileges.", authorities);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else  {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/userDetails")
    public ResponseEntity<MessageDto> message(Authentication authentication) {

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

        List<String> authorities = fetchedUser.getAuthorities().stream()
                .map(authority -> authority.toString())
                .toList();

        MessageDto message = new MessageDto(
                "Hi " + email, authorities);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    private record MessageDto(String message, List<String> authorities) { }

    private String isEnded(int iteration,int listSize) {
        if (iteration < listSize - 1) {
            return ", ";
        } else {
            return ".";
        }
    }
}
