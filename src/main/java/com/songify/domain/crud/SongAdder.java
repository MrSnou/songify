package com.songify.domain.crud;

import com.songify.infrastructure.crud.song.util.SongDto;
import com.songify.infrastructure.crud.song.util.SongLanguageDto;
import com.songify.infrastructure.crud.song.dto.request.SongRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SongAdder {

    private final SongRepository songRepository;

    SongDto addSongFromSongDto(final SongRequestDto requestDto) {
        SongLanguageDto language = requestDto.language();
        SongLanguage songLanguage = SongLanguage.valueOf(language.name());

        Song save = new Song(requestDto.name(), requestDto.releaseDate(), requestDto.duration(),  songLanguage);
        Song saved = songRepository.save(save);
        return new SongDto(saved.getId(), saved.getName(), saved.getDuration());
    }

    Song addSong(final SongRequestDto requestDto) {
        SongLanguageDto language = requestDto.language();
        SongLanguage songLanguage = SongLanguage.valueOf(language.name());

        Song save = new Song(requestDto.name(), requestDto.releaseDate(), requestDto.duration(),  songLanguage);
        Song saved = songRepository.save(save);
        return songRepository.save(save);
    }


}
