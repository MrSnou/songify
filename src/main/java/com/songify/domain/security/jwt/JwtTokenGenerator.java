package com.songify.domain.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.songify.infrastructure.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final RSAPrivateKey rsaPrivateKey;
    @Value("${jwt.expiration.seconds}")
    private int expiresIn;

    public String generateToken(SecurityUser securityUser) {
        if (securityUser == null) return null;
        if (!securityUser.isEnabled()) return null;

        String email = securityUser.getUsername();
        List<@Nullable String> authorities = securityUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        return  JWT.create()
                .withSubject(email)
                .withClaim("roles", authorities)
                .withExpiresAt(Instant.now().plusSeconds(expiresIn)) // 30 min
                .sign(Algorithm.RSA256(null, rsaPrivateKey));
    }
}
