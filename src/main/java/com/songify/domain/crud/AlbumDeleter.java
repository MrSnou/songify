package com.songify.domain.crud;

import com.songify.domain.crud.Exceptions.AlbumNotEmptyException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Log4j2
class AlbumDeleter {

    private final AlbumRepository albumRepository;

    void deleteById(Album album) {
        if (album.getSongs().isEmpty()) {
            albumRepository.deleteById(album.getId().longValue());
        } else {
            throw new AlbumNotEmptyException("Album is not empty, cannot delete");
        }
    }


}
