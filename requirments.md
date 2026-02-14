### Songify: Aplikacja do zarządzania albumami, artystami i piosenkami

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

17. Gdy nie ma przypisanego gatunku muzycznego do piosenki, to wyświetlamy "default"

~~18. można wyświetlać wszystkie piosenki~~

19. można wyświetlać wszystkie gatunki

~~20. można wyświetlać wszystkich artystów~~

21. można wyświetlać wszystkie albumy

~~22. można wyświetlać konkretne albumy z artystami oraz piosenkami w albumie~~

23. można wyświetlać konkretne gatunki muzyczne wraz z piosenkami
24. można wyświetlać konkretnych artystów wraz z ich albumami
~~25. TODO SQLException Handlers~~


CQRS -  Command | Query | Request Separation


Happy Path :
- User tworzy album :
    {
        albumName   = "EminemAlbum1",
        albumId     = 1
        [   
            genreId     = 1
            genreName   = "Rap"
            {
                songId      = 1
                songName    = "Till i collapse"
            }
            {
                songId       = 2
                songName     = "Lose Yourself"
            }
        ]
    }

Given there is no songs, artists, albums and genres created before

1. When I go to /song then I can see no songs
2. When I post to /song with Song "Till I collapse" is returned with id 1
3. When I post to /song with Song "Lose Yourself" then Song "Lose Yourself" is returned with id 2
4. When I go to /genre then I can see no genres
5. When I post to /genre with Genre "Rap" then Genre "Rap" is returned with id 1
6. When I go to /song/1 then I can see default genre
7. When I put to /song/1/genre/1 then Genre with id 1 ("Rap") is added to Song with id 1 ("Till I collapse")
8. When I go to /song/1 then I can see "Rap" genre
9. When I put to /song/2/genre/1 then Genre with id 1 ("Rap") is added to Song with id 2 ("Lose Yourself")
10. When I go to /album then I can see no albums
11. When I post to /album with Album "EminemAlbum1" and Song with id 1 then Album "EminemAlbum1" is returned with id 1
12. When I go to /album/1 then I can see song with id 1 added to it
13. When I put to /album/1/song/1 then Song with id 1 ("Till I collapse") is added to Album with id 1 ("EminemAlbum1")
14. When I put to /album/1/song/2 then Song with id 2 ("Lose Yourself") is added to Album with id 1 ("EminemAlbum1")
15. When I go to /album/1/song then I can see 2 songs (id 1, id 2)
16. When I post to /artist with Artist "Eminem" then Artist "Eminem" is returned with id 1
17. When I put to /album/1/artist/2 then Artist with id 1 ("Eminem") is added to Album with id 1



Co jest możliwe w 7dni/30dni/90dni

7 : 
~~- harmonogram
- zorganizowanie rodziny pod nie przeszkadzanie (włącznie z kotem)~~

30: 
~~?zmiana pracy~~


90 Dni: 
- Skontaktowanie się z pauliną, bo pracuje w Allegro
- Ukończenie kursu.
26.11.2025 - 16.01.2026 (50 dni) 1/3 >> 30.04.2026 FINISH


Koło wartości:

533+140 Stron Java





