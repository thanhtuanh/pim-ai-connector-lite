package com.dnguyen.pes.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VisionService {

  private static final Logger logger = LoggerFactory.getLogger(VisionService.class);

  @Value("${google.vision.api-key}")
  private String apiKey;

  @Value("${OPENAI_API_KEY:demo}")
  private String openAiKey;

  private final RestTemplate restTemplate;
  private final ConfigService configService;

  public VisionService(ConfigService configService) {
    this.configService = configService;
    this.restTemplate = new RestTemplate();
  }

  public String analyzeImage(byte[] imageBytes) {
    try {
      // Vision API Request vorbereiten
      String base64Image = Base64.getEncoder().encodeToString(imageBytes);
      Map<String, Object> image = Map.of("content", base64Image);
      Map<String, Object> feature = Map.of("type", "TEXT_DETECTION");
      Map<String, Object> request = Map.of("image", image, "features", List.of(feature));
      Map<String, Object> payload = Map.of("requests", List.of(request));

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

      String url = "https://vision.googleapis.com/v1/images:annotate?key=" + apiKey;
      ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

      List<Map<String, Object>> responses = (List<Map<String, Object>>) response.getBody().get("responses");
      if (responses == null || responses.isEmpty()) {
        return "⚠️ Keine Antwort von Google Vision erhalten.";
      }

      Map<String, Object> annotation = responses.get(0);
      Map<String, Object> textAnnotation = (Map<String, Object>) annotation.get("fullTextAnnotation");
      if (textAnnotation == null || !textAnnotation.containsKey("text")) {
        return "⚠️ Kein Text im Bild erkannt.";
      }

      return textAnnotation.get("text").toString().trim();

    } catch (Exception e) {
      logger.error("Fehler bei der Bildanalyse: " + e.getMessage(), e);
      return "❌ Fehler bei der Bildanalyse: " + e.getMessage();
    }
  }

  public String generateGptDescription(Map<String, String> paramValues) {
    try {
      // GPT-Prompt aus der Konfiguration generieren
      String prompt = configService.generatePrompt(paramValues);
      logger.info("Generiere GPT-Beschreibung mit Prompt: {}", prompt);

      Map<String, Object> requestBody = Map.of(
          "model", "gpt-3.5-turbo",
          "messages", List.of(
              Map.of("role", "user", "content", prompt)),
          "max_tokens", 350,
          "temperature", 0.7);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(openAiKey);

      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

      ResponseEntity<Map> response = restTemplate.postForEntity(
          "https://api.openai.com/v1/chat/completions",
          entity,
          Map.class);

      List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
      if (choices != null && !choices.isEmpty()) {
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        if (message != null && message.containsKey("content")) {
          return message.get("content").toString().trim();
        }
      }

      return "⚠️ Keine Antwort von GPT erhalten.";
    } catch (Exception e) {
      logger.error("Fehler bei der GPT-Anfrage: " + e.getMessage(), e);
      return "❌ Fehler bei der GPT-Anfrage: " + e.getMessage();
    }
  }
}