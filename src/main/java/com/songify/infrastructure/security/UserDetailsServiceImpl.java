package com.songify.infrastructure.security;

import com.songify.domain.usercrud.User;
import com.songify.domain.usercrud.UserRepository;
import com.songify.domain.usercrud.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsManager {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(final UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new UserAlreadyExistsException("User with email " + user.getUsername() + " already exists.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = new User(
                user.getUsername(),
                encodedPassword,
                true,
                List.of(DEFAULT_USER_ROLE)
        );
        userRepository.save(newUser);

    }

    @Override
    public void updateUser(final UserDetails user) {

    }

    @Override
    public void deleteUser(final String username) {

    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findFirstByEmailIgnoreCase(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new RuntimeException("User with email " + username + " not found."));
    }

    @Override
    public boolean userExists(final String username) {
        return userRepository.existsByEmailIgnoreCase(username);
    }
}
