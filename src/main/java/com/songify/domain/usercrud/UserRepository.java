package com.songify.domain.usercrud;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    User save(User user);

    Optional<User> findFirstByEmailIgnoreCase(String email);

    Boolean existsByEmailIgnoreCase(String email);
}
