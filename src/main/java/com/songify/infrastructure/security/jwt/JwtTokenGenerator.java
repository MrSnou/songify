package com.songify.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.songify.infrastructure.security.SecurityUser;
import io.micrometer.core.instrument.Clock;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@AllArgsConstructor
class JwtTokenGenerator {

    static final String ROLES_CLAIM_NAME = "roles";

    private final AuthenticationManager authenticationManager;
    private final JwtConfigurationProperties properties;
    private final KeyPair keyPair;

    String authenticateAndGenerateToken(final String username, final String password) throws NoSuchAlgorithmException {
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticate);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofMinutes(properties.expirationMinutes()));
        // Symetric Key Algorithm - Simpler to implement but less secure as the same key is used for signing and verification
//        Algorithm algorithm = Algorithm.HMAC256(properties.secret());
        // Asymetric Key Algorithm - Requires additional configuration for public/private keys
        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) keyPair.getPrivate());


        return JWT.create()
                .withSubject(securityUser.getUsername())
                .withIssuedAt(Instant.now())
                .withExpiresAt(expiresAt)
                .withIssuer(properties.issuer())
                .withClaim(ROLES_CLAIM_NAME, securityUser.getAuthoritiesAsString())
                .sign(algorithm);
    }
}
