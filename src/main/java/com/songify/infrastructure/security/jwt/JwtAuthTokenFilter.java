package com.songify.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final KeyPair keyPair;
    private final JwtConfigurationProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader("Authorization");
        if (authToken == null) {
            filterChain.doFilter(request, response);
            return;
        }


        //Symetric Key approach
        String secret = properties.secret();
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        //Asymetric Key approach
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), null))
//                .build();
        String token = authToken.substring(7);
        DecodedJWT decodedJWTToken = jwtVerifier.verify(token);
        String loginUsername = decodedJWTToken.getSubject();
        Collection<SimpleGrantedAuthority> roles = decodedJWTToken.getClaim("roles")
                .asList(String.class).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUsername, null, roles);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
