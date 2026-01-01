package com.aipitstop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AiService {

    private final WebClient webClient;

    @Value("${GROQ_API_KEY:}")
    private String groqApiKey;

    @Value("${STABILITY_API_KEY:}")
    private String stabilityApiKey;

    @Value("${STABILITY_API_URL:https://api.stability.ai/v1}")
    private String stabilityApiUrl;

    public AiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> generateText(String prompt) {
        if (groqApiKey == null || groqApiKey.isBlank()) {
            return Mono.just("[Mocked] Groq API key not set. Prompt was: " + prompt);
        }
        // Example Groq request (adjust per Groq API spec)
        return webClient.post()
                .uri("https://api.groq.com/v1/generate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + groqApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("prompt", prompt)))
                .retrieve()
                .bodyToMono(Map.class)
                .map(Map::toString)
                .onErrorReturn("[Error] Failed to call Groq API");
    }

    public Mono<String> generateImage(String prompt) {
        if (stabilityApiKey == null || stabilityApiKey.isBlank()) {
            return Mono.just("[Mocked] Stability API key not set. Prompt was: " + prompt);
        }
        // Example Stability call - this is a placeholder. Replace with actual endpoint and payload.
        return webClient.post()
                .uri(stabilityApiUrl + "/v1/generation/text-to-image")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + stabilityApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("prompt", prompt)))
                .retrieve()
                .bodyToMono(Map.class)
                .map(Map::toString)
                .onErrorReturn("[Error] Failed to call Stability API");
    }
}
