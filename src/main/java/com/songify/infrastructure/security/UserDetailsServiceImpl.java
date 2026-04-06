package com.songify.infrastructure.security;

import com.songify.domain.usercrud.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

@AllArgsConstructor
class UserDetailsServiceImpl implements UserDetailsManager {

    private final UserRepository userRepository;


    @Override
    public void createUser(final UserDetails user) {
//        userRepository.save()
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
