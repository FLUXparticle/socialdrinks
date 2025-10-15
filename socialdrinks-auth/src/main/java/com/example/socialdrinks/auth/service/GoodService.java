package com.example.socialdrinks.auth.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GoodService {

    private final Map<String, String> texts = new ConcurrentHashMap<>();

    public void saveText(String username, String text) {
        if (username == null) {
            return;
        }
        texts.put(username, text == null ? "" : text);
    }

    public String getText(String username) {
        if (username == null) {
            return "";
        }
        return texts.getOrDefault(username, "");
    }

    public Map<String, String> getAllTexts() {
        return Collections.unmodifiableMap(new HashMap<>(texts));
    }
}
