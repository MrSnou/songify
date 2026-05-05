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


:
- User tworzy album :
{
  "id": 1,
  "name": "Album 1",
  "releaseDate": "2023-01-15T11:00:00Z",
  "artists": [
    {
      "id": 1,
      "name": "Ariana Grande"
    },
    {
      "id": 3,
      "name": "Taylor Swift"
    },
    {
      "id": 2,
      "name": "Ed Sheeran"
    }
    ],
  "songs": [
    {
      "id": 1,
      "name": "Bohemian Rhapsody",
      "duration": 355,
      "releaseDate": "2023-01-01T10:00:00Z",
      "genreDto": {
        "id": 1,
        "name": "Default"
      }
    },
    {
      "id": 2,
      "name": "Shape of You",
      "duration": 235,
      "releaseDate": "2023-01-02T11:15:00Z",
      "genreDto": {
        "id": 2,
        "name": "Rock"
      }
    }
  ]
}





