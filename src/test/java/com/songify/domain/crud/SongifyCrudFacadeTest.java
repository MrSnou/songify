package com.songify.domain.crud;

import com.songify.infrastructure.crud.artist.ArtistDto;
import com.songify.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongifyCrudFacadeTest {

    SongifyCrudFacade songifyCrudFacade = createSongifyCrudFacade(
            new InMemorySongRepository(),
            new InMemoryGenreRepository(),
            new InMemoryArtistRepository(),
            new ImMemoryAlbumRepository()
    );

    private static SongifyCrudFacade createSongifyCrudFacade(final SongRepository songRepository,
                                                             final GenreRepository genreRepository,
                                                             final ArtistRepository artistRepository,
                                                             final AlbumRepository albumRepository) {


        AlbumDeleter albumDeleter = new AlbumDeleter(albumRepository);
        AlbumRetriever albumRetriever = new AlbumRetriever(albumRepository);
        ArtistRetriever artistRetriever = new ArtistRetriever(artistRepository);
        ArtistAssigner artistAssigner = new ArtistAssigner(albumRetriever, artistRetriever);
        ArtistUpdater artistUpdater = new ArtistUpdater(artistRetriever);
        GenreRetriever genreRetriever = new GenreRetriever(genreRepository);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        GenreUpdater genreUpdater = new GenreUpdater(genreRepository);
        SongAdder songAdder = new SongAdder(songRepository);
        SongRetriever songRetriever = new SongRetriever(songRepository, genreRetriever);
        AlbumUpdater albumUpdater = new AlbumUpdater(albumRepository, artistRetriever, songRetriever);
        GenreDeleter genreDeleter = new GenreDeleter(genreRepository,songRetriever);
        SongDeleter songDeleter = new SongDeleter(songRepository, songRetriever, genreDeleter);
        ArtistDeleter artistDeleter = new ArtistDeleter(artistRepository, artistRetriever, albumDeleter, songDeleter, albumRetriever);
        SongUpdater songUpdater = new SongUpdater(songRepository, songRetriever, albumUpdater, genreRetriever);
        AlbumAdder albumAdder = new AlbumAdder(songRetriever, albumRepository);
        ArtistAdder artistAdder = new ArtistAdder(artistRepository, albumAdder,  songAdder);

        return new SongifyCrudFacade(songAdder, songUpdater, songDeleter, songRetriever,
                artistAdder, artistUpdater, artistDeleter, artistAssigner, artistRetriever,
                genreAdder, genreDeleter, genreUpdater, genreRetriever,
                albumAdder, albumUpdater, albumDeleter, albumRetriever);
    }

    @Test
    public void first() {

        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Shawn Mendes")
                .build();
        ArtistDto artistDto = songifyCrudFacade.addArtist(requestDto);


    }

    @Test
    public void second() {

    }

    @Test
    public void third() {

    }

}