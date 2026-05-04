package com.songify.infrastructure.security;

import com.songify.domain.security.jwt.JwtTokenGenerator;
import com.songify.infrastructure.security.jwt.TokenCookieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserDetailsManager userDetailsManager;
    private final TokenCookieService tokenCookieService;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        SecurityUser userFromDb = (SecurityUser) userDetailsManager.loadUserByUsername(oidcUser.getEmail());
        tokenCookieService.addTokenCookieToResponse(response, jwtTokenGenerator.generateToken(userFromDb));
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
