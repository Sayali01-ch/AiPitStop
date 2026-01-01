package com.aipitstop.controller;

import com.aipitstop.service.AiService;
import com.aipitstop.service.PdfService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AiController {

    private final AiService aiService;
    private final PdfService pdfService;

    public AiController(AiService aiService, PdfService pdfService) {
        this.aiService = aiService;
        this.pdfService = pdfService;
    }

    @PostMapping(path = "/ai/text", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> generateText(@RequestBody Map<String, String> body) {
        String prompt = body.getOrDefault("prompt", "");
        return aiService.generateText(prompt)
                .map(result -> ResponseEntity.ok(Map.of("result", result)));
    }

    @PostMapping(path = "/ai/image", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> generateImage(@RequestBody Map<String, String> body) {
        String prompt = body.getOrDefault("prompt", "");
        return aiService.generateImage(prompt)
                .map(result -> ResponseEntity.ok(Map.of("image", result)));
    }

    @PostMapping(path = "/doc/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> extractPdfText(@RequestPart("file") MultipartFile file) throws Exception {
        String text = pdfService.extractText(file);
        return ResponseEntity.ok(Map.of("text", text));
    }
}
