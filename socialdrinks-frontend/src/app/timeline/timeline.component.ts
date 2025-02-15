import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

export interface Cocktail {
    id: number;
    name: string;
    // ggf. weitere Eigenschaften
}

export interface User {
    username: string;
}

export interface Post {
    id: number;
    creator: User;
    cocktailId: number;
    cocktailName: string;
    rating: number;
    createdAt: string;
}

@Component({
    selector: 'app-timeline',
    templateUrl: './timeline.component.html',
    styleUrls: ['./timeline.component.css']
})
export class TimelineComponent implements OnInit, OnDestroy {
    // Fehler- bzw. Statusmeldungen
    errorPost: string = '';
    errorSearch: string = '';

    // Timeline-Posts
    timeline: Post[] = [];

    // Dropdown für Cocktails und Bewertung
    cocktails: Cocktail[] = [];
    selectedCocktail!: Cocktail;
    rating: number = 1;  // Default: Bewertung 1

    // Suchfunktion (optional)
    searchQuery: string = '';
    searchResults: Post[] = [];

    // EventSource für SSE-Timeline
    private eventSource: EventSource | null = null;

    constructor(private http: HttpClient, private ngZone: NgZone) { }

    ngOnInit(): void {
        this.loadCocktails();
        this.subscribeToTimeline();
    }

    ngOnDestroy(): void {
        if (this.eventSource) {
            this.eventSource.close();
        }
    }

    /**
     * Lädt die Liste der Cocktails von /api/cocktails.
     */
    loadCocktails(): void {
        this.http.get<Cocktail[]>('/api/cocktails').subscribe({
            next: (data) => {
                this.cocktails = data;
                if (data.length > 0) {
                    this.selectedCocktail = data[0];
                }
            },
            error: (err) => {
                console.error('Error loading cocktails:', err);
            }
        });
    }

    /**
     * Abonnieren der Timeline-Posts via Server-Sent Events (SSE) von /api/feed/posts.
     */
    subscribeToTimeline(): void {
        this.eventSource = new EventSource('/api/feed/posts');
        this.eventSource.onmessage = (event) => {
            this.ngZone.run(() => {
                const updatedPost = JSON.parse(event.data) as Post;
                const index = this.timeline.findIndex(post => post.id === updatedPost.id);
                if (index !== -1) {
                    // Existierenden Post aktualisieren
                    this.timeline[index] = updatedPost;
                } else {
                    // Neuen Post hinzufügen
                    this.timeline.push(updatedPost);
                }
            });
        };
        this.eventSource.onerror = (event) => {
            console.error("EventSource error:", event);
            // Versuche die Verbindung nach 5 Sekunden erneut herzustellen
            setTimeout(() => {
                this.subscribeToTimeline();
            }, 5000);
        };
    }

    /**
     * Erstellt einen neuen Post (Rating) durch Absenden eines JSON-Objekts an /api/feed/posts.
     * Erwartetes JSON-Objekt:
     * {
     *   "cocktailId": <number>,
     *   "cocktailName": <string>,
     *   "rating": <number>  // Bewertung von 1 bis 5
     * }
     */
    createPost(): void {
        if (!this.selectedCocktail) {
            this.errorPost = "Bitte wähle einen Cocktail aus.";
            return;
        }
        if (this.rating < 1 || this.rating > 5) {
            this.errorPost = "Die Bewertung muss zwischen 1 und 5 liegen.";
            return;
        }

        const payload = {
            cocktailId: this.selectedCocktail.id,
            cocktailName: this.selectedCocktail.name,
            rating: this.rating
        };

        this.http.post<{ success: string, error: string }>('/api/feed/posts', payload).subscribe({
            next: (data) => {
                if (data.error) {
                    this.errorPost = `Server Error: ${data.error}`;
                } else {
                    this.errorPost = '';
                    // Optional: Reset der Bewertung oder Anzeige einer Bestätigung
                }
            },
            error: (err) => {
                this.errorPost = `Client Error: ${err.message}`;
            }
        });
    }

    /**
     * Sucht in den Timeline-Posts via /api/feed/posts/search?q=...
     */
    searchPosts(): void {
        if (this.searchQuery.trim() === '') {
            this.errorSearch = "Suchbegriff darf nicht leer sein.";
            return;
        }
        this.http.get<{ searchResults: Post[], error: string }>(`/api/feed/search?q=${this.searchQuery.trim()}`).subscribe({
            next: (data) => {
                if (data.error) {
                    this.errorSearch = `Server Error: ${data.error}`;
                } else {
                    this.errorSearch = '';
                    this.searchResults = data.searchResults || [];
                }
            },
            error: (err) => {
                this.errorSearch = `Client Error: ${err.message}`;
            }
        });
    }

}
