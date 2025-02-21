package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.model.entity.*;
import org.springframework.stereotype.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

import java.util.*;

@Service
public class CocktailRemoteService {

    private final WebClient webClient;

    /**
     * Mit Sinks.many().unicast().onBackpressureBuffer() erstellen wir einen Sink, der als Queue fungiert. Da es sich um einen unicast-Sink handelt, kann er genau einen Subscriber haben – ideal für unseren Zweck, wenn nur intern eine Verarbeitung stattfinden soll.
     */
    private final Sinks.Many<CocktailDetailRequest> detailRequestSink =
            Sinks.many().unicast().onBackpressureBuffer();

    /**
     * Mit detailRequestSink.asFlux().flatMap(CocktailDetailRequest::processDetailRequest, 2) werden alle Anfragen aus der Queue entnommen und parallel (aber maximal 2 gleichzeitig) verarbeitet. Die Methode processDetailRequest führt die tatsächliche HTTP-Anfrage aus und übergibt das Ergebnis an den ursprünglichen Request.
     */
    public CocktailRemoteService(WebClient webClient) {
        this.webClient = webClient;
        detailRequestSink.asFlux()
                .flatMap(CocktailDetailRequest::processDetailRequest, 2)
                .subscribe();
    }

    /**
     * Diese innere Klasse kapselt die ID des Cocktails sowie einen Sinks.One<CocktailDetails>, über den wir später das Ergebnis (bzw. einen Fehler) zurückgeben.
     */
    private class CocktailDetailRequest {
        final Long id;
        final Sinks.One<CocktailDetails> responseSink;

        CocktailDetailRequest(Long id, Sinks.One<CocktailDetails> responseSink) {
            this.id = id;
            this.responseSink = responseSink;
        }

        // Methode, die eine Detail-Anfrage verarbeitet.
        Mono<Void> processDetailRequest() {
            return getCocktailDetailsImpl(id)
                    .doOnNext(responseSink::tryEmitValue)
                    .doOnError(responseSink::tryEmitError)
                    .then(); // Wir brauchen hier nur ein Signal zum Abschluss.
        }

    }

    /**
     * Diese Methode bleibt in der Signatur unverändert und gibt einen Mono zurück. Allerdings wird die Anfrage nicht sofort ausgeführt, sondern in die Queue eingereiht. Der zurückgegebene Mono wird erst erfüllt, wenn die HTTP-Anfrage tatsächlich durchgeführt wurde.
     */
    public Mono<CocktailDetails> getCocktailDetails(Long id) {
        Sinks.One<CocktailDetails> responseSink = Sinks.one();
        CocktailDetailRequest request = new CocktailDetailRequest(id, responseSink);
        detailRequestSink.tryEmitNext(request);
        return responseSink.asMono();
    }

    public Flux<Cocktail> getAllCocktails() {
        return webClient.get()
                .uri("/api/cocktails")
                .retrieve()
                .bodyToFlux(Cocktail.class);
    }

    private Mono<CocktailDetails> getCocktailDetailsImpl(Long id) {
        System.out.println("> " + id);
        return webClient.get()
                .uri("/api/cocktails/" + id)
                .retrieve()
                .bodyToMono(CocktailDetails.class);
    }

    public Flux<Ingredient> getAllIngredients() {
        return webClient.get()
                .uri("/api/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class);
    }

    public Mono<Ingredient> getIngredientWithID(Long id) {
        return webClient.get()
                .uri("/api/ingredients/" + id)
                .retrieve()
                .bodyToMono(Ingredient.class);
    }

    public Flux<Cocktail> getPossibleCocktails(Collection<Long> ingredientIDs) {
        // Erstelle das Anfrage-Body-Objekt
        Map<String, Collection<Long>> requestBody = new HashMap<>();
        requestBody.put("ingredientIDs", ingredientIDs);

        // Führe den POST-Request aus
        return webClient.post()
                .uri("/api/possible")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(Cocktail.class);
    }

}
