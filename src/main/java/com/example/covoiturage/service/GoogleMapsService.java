/*package com.example.covoiturage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GoogleMapsService {

    private final String apiKey = "VOTRE_CLE_API"; // Remplacez par votre clé API
    private final WebClient webClient;

    public GoogleMapsService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://maps.googleapis.com/maps/api")
                .build();
    }

    // Méthode pour calculer la distance et le temps de trajet
    public String getDistanceAndDuration(String origin, String destination) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/distancematrix/json")
                        .queryParam("origins", origin)
                        .queryParam("destinations", destination)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response; // La réponse JSON contient les informations
    }

    // Méthode pour obtenir un itinéraire
    public String getRoute(String origin, String destination) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/directions/json")
                        .queryParam("origin", origin)
                        .queryParam("destination", destination)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response; // La réponse JSON contient l'itinéraire
    }
}
*/