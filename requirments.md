### Songify: Aplikacja do zarządzania albumami, artystami i piosenkami

*Application Implementation*

~~1. Można dodać artystę (nazwa artysty)~~

~~2. Można dodać gatunek muzyczny (nazwa gatunku)~~

~~3. Można dodać album (tytuł, czas trwania, data wydania oraz artystę, do którego należy)~~

~~4. można dodawać piosenkę (tytuł, czas trwania, data wydania, język piosenki)~~

~~5. Można usunąć artystę (usuwamy wtedy jego piosenki oraz albumy jeśli są na wyłączność jego)~~

~~6. Można dodać artystę wraz z albumem oraz piosenką~~

~~6. można usunąć gatunek muzyczny (tylko gdy nie jest do niego przypisana żadna piosenka)~~

~~7. można usunąć album (tylko gdy nie jest do niego przypisana żadna piosenka)~~

~~8. można usunąć piosenkę (wyłącznie)~~ 

~~9. można edytować nazwę artysty~~

~~10. można edytować nazwę gatunku muzycznego~~ 

~~11. można edytować album (dodawać piosenki, artystów, zmienić nazwę albumu)~~

~~12. można edytować piosenkę (czas trwania, nazwę piosenki)~~

~~13. można przypisywać piosenki do albumów (album może mieć więcej artystów, artysta może mieć kilka albumów)~~

~~14. można przypisać piosenki do artystów (poprzez album)~~

~~15. można przypisać artystów do albumów (album może mieć więcej artystó, artysta może mieć kilka albumów)~~

~~16. można przypisać tylko jeden gatunek muzyczny do piosenki~~

~~17. Gdy nie ma przypisanego gatunku muzycznego do piosenki, to wyświetlamy "default"~~

~~18. można wyświetlać wszystkie piosenki~~

~~19. można wyświetlać wszystkie gatunki~~

~~20. można wyświetlać wszystkich artystów~~

~~21. można wyświetlać wszystkie albumy~~

~~22. można wyświetlać konkretne albumy z artystami oraz piosenkami w albumie~~

~~23. można wyświetlać konkretne gatunki muzyczne wraz z piosenkami~~

~~24. można wyświetlać konkretnych artystów wraz z ich albumami~~

~~25. TODO SQLException Handlers~~

*Security Implementation*

~~26. Każdy bez uwierzytelnienia (authentication) może przeglądać piosenki, albumy itp. (gość)~~
~~27. Są 2 role: Role_USER i Role_Admin~~
28. ~~Używanie bezstanowego tokena JWT (uzyskuje go po zalogowaniu) - własna implementacja authorization~~ i potem oauth google
29. Tylko Admin może przejrzeć loginy i role użytkowników endpoint /users
~~30. Aby zostać użytkownikiem trzeba się zarejestrować login/hasło — własna implementacja i google~~
~~31. Zapisujemy użytkownika i Admina do bazy danych (w przypadku własnej implementacji) - admin tworzony w migracji flyway~~
32. ~~Użytkownik (Role_USER) może wyświetlać piosenki, ale nie może zarządzać~~
(w przyszłości użytkownik może mieć swój profil, a tam "Ulubione Piosenki").
33. ~~Tylko Admin może zmieniać stan aplikacji (usuwać, dodawać, edytować piosenki/albumy itp.)~~
34. ~~Chcemy mieć szyfrowanie HTTPS, certyfikat wygenerowany ręcznie openssl~~
~~35. Chcemy mieć obsługę CORS - Zapytania z domeny frontend'owej~~
36. ~~Chcemy Zabezpieczenie CSRF, bo będzie frontend~~
~~38. Testy Integracyjne i Jednostkowe~~





CQRS -  Command | Query | Request Separation


Happy Path :
- User tworzy album :
    {
        albumName   = "EminemAlbum1",
        albumId     = 1
        artist {
            artistId = 1,
            artistName = 1
        }
        [
            {
                songId      = 1
                songName    = "Till i collapse"
                {
                    genreId = 1
                    genreName = "Default"
                }
            }
            {
                songId       = 2
                songName     = "Lose Yourself"
                {
                    genreId = 2
                    genreName = "Rap"
                }
            }
        ]
      }

Given there is no songs, artists, albums and genres created before

Happy Path V2 (after refactor)

1. When I go to LH:8082/api/v1/songs/(Http_GetMethod), I can see no songs
2. When I post to LH:8082/api/v1/songs/(Http_PostMethod with body) with song "Till I collapse", 
   then I can see that posted song is returned with id 1.
3. When I post to LH:8082/api/v1/songs/(Http_PostMethod with body) with song "Lose Yourself",
   then I can see that posted song is returned with id 2.
4. When I go to LH:8082/api/v1/genres/(Http_GetMethod), I can see only "Default" Genre with id 1.
5. When I post to LH:8082/api/v1/genres/(Http_PostMethod with body) with Genre "Rap",
   then I can see added Genre with id 2.
6. When I go to LH:8082/api/v1/genre/1, I can see Default genre with id 1.
7. When I update to (Http_PostMethod with PathVariable songId and UpdateGenreDto body) 
   LH:8082/api/v1/updateSongGenre/1 and UpdateGenreBody(genreId - 2), then I can see that Genre was added to song with id 1.
8. When I go to LH:8082/api/v1/songs/1, I can see song with "Rap" Genre.
9. When I go to LH:8082/api/v1/albums/, I can see no albums.
10. When I post to LH:8082/api/v1/albums/(Http_PostMethod with body) with album "Eminem_Album_1" and song id 1,
    then album "Eminem_Album_1" is returned with id 1.
11. When I go to LH:8082/api/v1/albums/1, I can see no albums because there is no artists in the system.
12. When I post to LH:8082/api/v1/artists/(Http_PostMethod with body) with Artist "Eminem",
    then artist "Eminem" is returned with id 1.
13. When I put to LH:8082/api/v1/artists/addArtistToAlbum/{artistId - 1}/{albumId - 1}, 
    then I can see that Artist with id 1 was added to Album with id 1.
14. When I go to LH:8082/api/v1/albums/1, I can see album with single song with id 1, and artist with id 1.
15. When I put to LH:8082/api/v1/albums/(Http_PostMethod with body) with album id 1 and song id 2 ("Lose Yourself"), 
    then Song with id 2 us added to Album with id 1 ("Eminem_Album_1").
16. When I go to Lh:8082/api/v1/albums/1, then I can see album with 2 songs (songId 1, songId 2) and one artist (artistId 1).
  





