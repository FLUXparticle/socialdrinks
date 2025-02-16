package com.example.socialdrinks.app.service;

import com.example.socialdrinks.app.entity.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class DataInitializer {

    private final BarService barService;

    @Autowired
    public DataInitializer(BarService barService) {
        this.barService = barService;
    }

    @PostConstruct
    public void init() {
        if (barService.getAllBars().isEmpty()) {
            for (int i = 1; i <= 100; i++) {
                Bar bar = new Bar();
                bar.setName("Bar " + i);
                bar.setEmail("bar" + i + "@example.com");
                bar.setAddress("Address " + i);
                barService.saveBar(bar);
            }
        }
    }

}
