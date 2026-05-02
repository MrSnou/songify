package com.songify.domain.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;

@Component
@RequiredArgsConstructor
class JwtTokenValidator {

    private final RSAPublicKey rsaPublicKey;

    public DecodedJWT validate(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.RSA256(rsaPublicKey, null)).build().verify(token);
        return decodedJWT;
    }
}
