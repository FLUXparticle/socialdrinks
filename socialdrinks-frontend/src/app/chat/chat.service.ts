import {Injectable, NgZone} from '@angular/core';
import {Client, Message} from '@stomp/stompjs';
import {Observable, Subject} from 'rxjs';

export interface ChatMessage {
    username: string;
    message: string;
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {
    private stompClient: Client;
    private messages: ChatMessage[] = [];
    private messagesSubject: Subject<ChatMessage[]> = new Subject<ChatMessage[]>();
    public messages$: Observable<ChatMessage[]> = this.messagesSubject.asObservable();

    constructor(private ngZone: NgZone) {
        // Dynamische WebSocket-URL für lokale und produktive Umgebungen
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const host = window.location.hostname;
        const port = window.location.port ? ':' + window.location.port : '';

        this.stompClient = new Client({
            brokerURL: `${protocol}//${host}${port}/api/chat/ws`, // Endpoint aus Spring WebSocket Config
            reconnectDelay: 5000, // Automatische Wiederverbindung nach 5 Sekunden
            debug: (str: string) => {
                console.log(`[STOMP Debug] ${str}`);
            }
        });

        this.stompClient.onConnect = () => {
            console.log('[STOMP] Verbunden mit WebSocket');

            // Abonniert den Channel für eingehende Nachrichten
            this.stompClient.subscribe('/topic/messages', (message: Message) => {
                this.ngZone.run(() => {
                    try {
                        const chatMessage: ChatMessage = JSON.parse(message.body);
                        this.messages.push(chatMessage);
                        this.messagesSubject.next([...this.messages]); // Neues Array auslösen, um UI zu aktualisieren
                        console.log('[STOMP] Empfangene Nachricht:', chatMessage.message);
                    } catch (error) {
                        console.error('[STOMP] Fehler beim Parsen der Nachricht:', error);
                    }
                });
            });
        };

        this.stompClient.onStompError = (frame) => {
            console.error('[STOMP] Fehler im STOMP-Protokoll:', frame);
        };

        this.stompClient.onWebSocketError = (event) => {
            console.error('[STOMP] WebSocket-Fehler:', event);
        };

        this.stompClient.onDisconnect = () => {
            console.log('[STOMP] Verbindung geschlossen');
        };

        // Aktiviert die WebSocket-Verbindung
        this.stompClient.activate();
    }

    sendMessage(message: string) {
        if (this.stompClient && this.stompClient.connected) {
            // const chatMessage: ChatMessage = { name: 'User123', textMessage: message };
            this.stompClient.publish({ destination: '/app/send-message', body: message });
            console.log('[STOMP] Nachricht gesendet:', message);
        } else {
            console.error('[STOMP] Verbindung nicht aktiv – Nachricht nicht gesendet.');
        }
    }
}
