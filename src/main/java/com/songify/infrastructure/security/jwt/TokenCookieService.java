package com.songify.infrastructure.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenCookieService {

    @Value("${jwt.expiration.seconds}")
    private int tokenExpirationTime;

    public void addTokenCookieToResponse(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("AuthorizationToken", token);
        cookie.setPath("/");
        cookie.setMaxAge(tokenExpirationTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
