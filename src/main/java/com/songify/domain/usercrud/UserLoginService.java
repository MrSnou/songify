package com.songify.domain.usercrud;

import com.songify.domain.security.jwt.JwtTokenGenerator;
import com.songify.domain.security.SecurityUser;
import com.songify.domain.usercrud.dto.LoginRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserLoginService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final AuthenticationManager authenticationManager;

    public String login(LoginRequestDto request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return jwtTokenGenerator.generateToken(securityUser);
    }
}
