package com.songify.infrastructure.security;

import com.songify.domain.usercrud.UserRegistrationPort;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
class UserRegistrationAdapter implements UserRegistrationPort {

    private final UserDetailsManager userDetailsManager;

    @Override
    public void register(String email, String password) {
        UserDetails user = User.builder()
                .username(email)
                .password(password)
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
    }

    @Override
    public boolean userExists(String email) {
        return userDetailsManager.userExists(email);
    }
}
