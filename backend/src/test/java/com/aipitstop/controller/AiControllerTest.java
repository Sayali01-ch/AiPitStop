package com.aipitstop.controller;

import com.aipitstop.service.AiService;
import com.aipitstop.service.PdfService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AiController.class)
public class AiControllerTest {

    @Autowired
    private WebTestClient webClient;

    // Provide simple stub beans to avoid Mockito inline mocking issues in this environment
    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfig {
        @org.springframework.context.annotation.Bean
        public com.aipitstop.service.AiService aiService() {
            return new com.aipitstop.service.AiService(org.springframework.web.reactive.function.client.WebClient.builder()){
                @Override
                public reactor.core.publisher.Mono<String> generateText(String prompt) {
                    return reactor.core.publisher.Mono.just("hello result");
                }

                @Override
                public reactor.core.publisher.Mono<String> generateImage(String prompt) {
                    return reactor.core.publisher.Mono.just("image-result");
                }
            };
        }

        @org.springframework.context.annotation.Bean
        public com.aipitstop.service.PdfService pdfService() {
            return new com.aipitstop.service.PdfService();
        }
    }

    @Test
    void generateTextReturnsMock() {
        webClient.post().uri("/api/ai/text")
                .bodyValue(java.util.Map.of("prompt", "hi"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.result").isEqualTo("hello result");
    }
}
