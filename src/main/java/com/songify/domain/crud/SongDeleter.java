package com.songify.domain.crud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongDeleter {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;

    void deleteById(Long id) {
        songRetriever.existsById(id);
        log.info("Deleting song with id: " + id);
        songRepository.deleteById(id);
    }
}
