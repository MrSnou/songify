package com.songify.infrastructure.security;

import com.songify.domain.security.jwt.JwtTokenGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${jwt.expiration.seconds}")
    private int tokenExpirationTime;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserDetailsManager userDetailsManager;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        SecurityUser userFromDb = (SecurityUser) userDetailsManager.loadUserByUsername(oidcUser.getEmail());
        setResponseCookie(response, jwtTokenGenerator.generateToken(userFromDb));
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void setResponseCookie(final HttpServletResponse response, final String token) {
        Cookie cookie =  new Cookie("AuthorizationToken", token);
        cookie.setPath("/");
        cookie.setMaxAge(tokenExpirationTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
