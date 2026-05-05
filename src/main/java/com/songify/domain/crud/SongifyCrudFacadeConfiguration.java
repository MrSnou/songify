package com.songify.domain.crud;

class SongifyCrudFacadeConfiguration {

    public static SongifyCrudFacade createSongifyCrudFacade(final SongRepository songRepository,
                                                            final GenreRepository genreRepository,
                                                            final ArtistRepository artistRepository,
                                                            final AlbumRepository albumRepository) {


        GenreService genreService = new GenreService(genreRepository, songRepository);
        ArtistService artistService = new ArtistService(artistRepository, songRepository, albumRepository);
        AlbumService albumService = new AlbumService(albumRepository, songRepository, artistRepository, artistService);
        SongService songService = new SongService(songRepository, genreRepository, albumService);




        return new SongifyCrudFacade(songService, albumService, genreService, artistService);
    }
}
