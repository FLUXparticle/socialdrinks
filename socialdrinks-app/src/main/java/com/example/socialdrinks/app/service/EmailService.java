package com.example.socialdrinks.app.service;

import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendEmail(String recipient) {
        try {
            // Simulierte Verzögerung
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error("Fehler bei der E-Mail-Versendung: ", e);
        }
        LOGGER.info("E-Mail gesendet an: {}", recipient);
    }

}
