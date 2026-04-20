package com.songify.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class CustomOidcUserService extends OidcUserService {

    private final UserDetailsService userDetailsService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        SecurityUser user;
        try {
            user = (SecurityUser) userDetailsService.loadUserByUsername(oidcUser.getEmail());
        } catch (UsernameNotFoundException e) {
            OAuth2Error oauthError = new OAuth2Error("invalid_token", "Username not found.", e.getMessage());
            throw new OAuth2AuthenticationException(oauthError, e.getMessage(), e);
        }
        OidcUserInfo userInfo = OidcUserInfo.builder()
                .claim("roles", user.getAuthoritiesAsString())
                .build();
        Collection<? extends GrantedAuthority> authorities = Stream.concat(
                        user.getAuthorities().stream(),
                        oidcUser.getAuthorities().stream()
                ).collect(Collectors.toSet());
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), userInfo, "sub");
    }
}
