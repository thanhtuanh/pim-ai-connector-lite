package com.dnguyen.pes.controller;

import com.dnguyen.pes.dto.EnrichmentRequest;
import com.dnguyen.pes.dto.EnrichmentResponse;
import com.dnguyen.pes.service.VisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrich")
@RequiredArgsConstructor
public class EnrichmentController {

        private final VisionService visionService;

        @Value("${OPENAI_API_KEY:demo}")
        private String openAiKey;

        private final RestTemplate restTemplate = new RestTemplate();

        @PostMapping
        public EnrichmentResponse enrich(@RequestBody EnrichmentRequest request) {
                EnrichmentResponse response = new EnrichmentResponse();
                response.setTitle("Demo-Produkt");
                response.setDescription("Dies ist eine Demo-Antwort.");
                return response;
        }

        @PostMapping("/gpt")
        public ResponseEntity<String> enrichWithGpt(@RequestBody EnrichmentRequest request) {
                String prompt = "Analysiere dieses Produkt:\n"
                                + "- Marke: " + request.getMarke() + "\n"
                                + "- Typ: " + request.getProduktart() + "\n"
                                + "- Farbe: " + request.getFarbe() + "\n"
                                + "- Material: " + request.getMaterial();

                Map<String, Object> requestBody = Map.of(
                                "model", "gpt-3.5-turbo",
                                "messages", List.of(
                                                Map.of("role", "user", "content", prompt)),
                                "max_tokens", 100,
                                "temperature", 0.7);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(openAiKey);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

                return restTemplate.postForEntity(
                                "https://api.openai.com/v1/chat/completions",
                                entity,
                                String.class);
        }
}
