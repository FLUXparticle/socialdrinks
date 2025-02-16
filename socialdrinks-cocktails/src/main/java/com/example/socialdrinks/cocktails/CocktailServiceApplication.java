package com.example.socialdrinks.cocktails;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.*;

@SpringBootApplication
@EntityScan(basePackages = "com.example.socialdrinks.model")
public class CocktailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocktailServiceApplication.class, args);
    }

}
