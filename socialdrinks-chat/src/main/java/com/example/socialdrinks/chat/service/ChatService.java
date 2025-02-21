package com.example.socialdrinks.chat.service;

import org.springframework.messaging.simp.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;

@Service
@EnableScheduling
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void enterRoom(String username) {
        String textMessage = username + " hat den Raum betreten.";
        sendLine(null, textMessage);
    }

    @Scheduled(fixedRate = 5000) // Alle 5 Sekunden
    public void executeTask() {
        sendLine(null, "Bist Du noch da?");
    }

    public void sendChatMessage(String username, String textMessage) {
        sendLine(username, textMessage);
    }

    private void sendLine(String username, String textMessage) {
        ChatMessage chatMessage = new ChatMessage(username, textMessage);
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
    }

    public static final class ChatMessage implements Serializable {
        private final String username;
        private final String message;

        public ChatMessage(String username, String message) {
            this.username = username;
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "ChatMessage[" +
                    "username=" + username + ", " +
                    "textMessage=" + message + ']';
        }

    }

}
