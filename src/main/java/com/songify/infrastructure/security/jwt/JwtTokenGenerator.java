package com.songify.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.songify.infrastructure.security.SecurityUser;
import io.micrometer.core.instrument.Clock;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@AllArgsConstructor
class JwtTokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final JwtConfigurationProperties properties;

    String authenticateAndGenerateToken(final String username, final String password) {
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticate);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = LocalDateTime.now(java.time.Clock.systemDefaultZone()).toInstant(ZoneOffset.UTC);

        Instant expiresAt = now.plus(Duration.ofMinutes(properties.expirationMinutes()));
        return JWT.create()
                .withSubject(securityUser.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(properties.issuer())
                .withClaim("roles", securityUser.getAuthoritiesAsString())
                .sign(null);
    }
}
