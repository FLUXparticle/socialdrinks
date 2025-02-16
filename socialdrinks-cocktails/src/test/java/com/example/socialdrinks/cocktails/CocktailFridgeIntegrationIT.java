package com.example.socialdrinks.cocktails;

import io.restassured.*;
import io.restassured.response.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CocktailFridgeIntegrationIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8082";
    }

    @Test
    void testGetCocktails() {
        Response response = given()
                .get("/api/cocktails");
        
        response.then().statusCode(200)
                .body("size()", equalTo(69));
    }

}
