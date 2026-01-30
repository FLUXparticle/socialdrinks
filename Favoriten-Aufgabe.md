# Favoriten – Aufgabenstellung

## Vorgabe
**Gegeben**
- Ein neues DTO `FavoriteCocktailDTO` ist vorhanden.
- Im MVC-Menü und im Angular-Menü gibt es einen neuen Menüpunkt „Favoriten“.
- Im Angular-Client sind Route, Template und ein `FavoritesController` angelegt.
  - Der Controller ruft `GET /api/favorites` und `POST /api/favorites/toggle` auf.
  - Die Favoriten-Seite listet Cocktails und zeigt `+`/`-` je nach Favorit-Status sowie die Favoriten-Anzahl.

## Hilfe
**Aufgabe**
- MongoDB-Dependency im Cocktail-Service hinzufügen.
- `Favorite`-Dokument und `FavoriteRepository` angelegen.
- Ein MVC-Template `favorites.html` schreiben.

**Hinweise zur Umsetzung**
- Favoriten liegen in MongoDB (Collection `favorites`) und werden pro Benutzer gespeichert.
- Toggle-Logik: existiert der Favorit → löschen, sonst anlegen.
- Für die Favoriten-Ansicht braucht ihr eine Liste aller Cocktails inkl. `favorite`-Flag und `favoriteCount`.

## Aufgabe
**MVC-Endpunkte**
- `GET /favorites` liefert die Favoriten-Seite mit Liste aller Cocktails und Favoriten-Status pro User.
- `POST /favorites/toggle` toggelt den Favoriten-Status für den aktuellen User.

**REST-Endpunkte**
- `GET /api/favorites` liefert eine Liste von `FavoriteCocktailDTO` (inkl. `favorite` und `favoriteCount`).
- `POST /api/favorites/toggle` erwartet `{ cocktailId }` und toggelt den Favoriten-Status.

**Weitere Änderungen**
- Favorite-Service: Ermittelt Favoriten pro User und aggregiert `favoriteCount` über alle Favoriten. (nicht DB)
- Gateway: Route für `/api/favorites/**` auf den Cocktail-Service.
