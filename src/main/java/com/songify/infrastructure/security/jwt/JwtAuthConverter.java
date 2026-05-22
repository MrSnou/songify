package com.songify.infrastructure.security.jwt;

import com.songify.domain.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    @Override
    public @Nullable JwtAuthenticationToken convert(final Jwt source) {
        String email = source.getClaimAsString("email");
        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(email);
        return new JwtAuthenticationToken(source, user.getAuthorities());
    }
}
