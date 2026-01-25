package com.songify.domain.crud;

import org.springframework.data.repository.CrudRepository;

interface GenreRepository extends CrudRepository<Genre, Long> {
    Genre save(Genre genre);
}
