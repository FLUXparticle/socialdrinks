package com.example.socialdrinks.chat.controller;

import com.example.socialdrinks.chat.service.*;
import org.slf4j.*;
import org.springframework.context.event.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.socket.messaging.*;

import java.security.*;

@Controller
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @EventListener
    public void handleSubscription(SessionSubscribeEvent event) {
        Principal principal = event.getUser();
        String name = "Unbekannt"; // principal.getName();

        LOGGER.info("Client connected: {}", name);

        chatService.enterRoom(name);
    }

    @MessageMapping("/send-message")
    public void sendMessage(String message, Principal principal) {
        String username = "Unbekannt";  // principal.getName();

        LOGGER.info("Message from user '{}' = {}", username, message);

        chatService.sendChatMessage(username, message);
    }

}
