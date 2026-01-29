# Warenkorb

## MVC

### Endpunkte
- `GET /cart` zeigt die Warenkorb-Seite mit zwei Listen: oben Zutaten im Warenkorb, unten alle Cocktails.
- `POST /cart/actions/add-cocktail` erwartet ein Form-Parameter `cocktailId` und fügt alle Zutaten des Cocktails in den Warenkorb (fehlende Zutaten werden ergänzt).
- `POST /cart/items/{ingredientId}/remove` entfernt eine einzelne Zutat aus dem Warenkorb.
- `POST /cart/clear` leert den Warenkorb komplett.

### Hinweise zur Umsetzung
- Session-Scope-Bean (`CartService`) hält die Zutaten-IDs (Set, keine Duplikate) und bietet Methoden zum Hinzufügen via Cocktail und zum Entfernen.
- Controller befüllt das Model mit `ingredients` (Cart-Inhalt) und `cocktails` (Auswahlliste unten).
- Template orientiert sich am Kühlschrank (Gateway): einfache Liste, Button-Formular für Aktionen.


## REST

### Endpunkte
- `GET /api/cart` liefert den aktuellen Warenkorb als JSON:
  - `ingredients`: Zutaten im Warenkorb
  - `cocktails`: komplette Cocktailliste zur Auswahl
- `POST /api/cart/actions/add-cocktail` nimmt JSON `{ "cocktailId": 123 }` und fügt Zutaten hinzu.
- `DELETE /api/cart/items/{ingredientId}` entfernt eine Zutat.
- `DELETE /api/cart` leert den Warenkorb.

### Hinweise zur Umsetzung
- Gateway-Route für `/api/cart/**` zum Cocktail-Service ergänzen.
- AngularJS-Client ist bereits umgesetzt und ruft die REST-Endpunkte auf.
