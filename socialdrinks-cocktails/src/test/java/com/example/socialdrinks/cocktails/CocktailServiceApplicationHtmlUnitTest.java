package com.example.socialdrinks.cocktails;

import org.htmlunit.*;
import org.htmlunit.html.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.server.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CocktailServiceApplicationHtmlUnitTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testGetCocktailsPage() throws Exception {
        try (WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage(baseUrl + "/cocktails");

            // Prüfen, ob die Seite den Titel "Cocktails" enthält
            assertThat(page.getTitleText()).isEqualTo("Cocktails");

            // Prüfen, ob eine Liste von Cocktails vorhanden ist
            assertThat(page.asNormalizedText()).contains("Andrea");
        }
    }

    @Test
    void testCocktailListHasExactly69Items() throws Exception {
        try (WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage(baseUrl + "/cocktails");

            // Finde die UL-Liste mit Cocktails
            HtmlElement ul = page.getFirstByXPath("//ul");

            // Finde alle LI-Elemente in der Liste
            List<HtmlElement> listItems = ul.getByXPath("./li");

            // Überprüfen, ob genau 69 Einträge existieren
            assertThat(listItems).hasSize(69);
        }
    }

}
