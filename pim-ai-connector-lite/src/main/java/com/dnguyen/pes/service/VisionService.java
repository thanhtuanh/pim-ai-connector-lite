package com.dnguyen.pes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VisionService {

  @Value("${google.vision.api-key}")
  private String apiKey;

  private final RestTemplate restTemplate = new RestTemplate();

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
      e.printStackTrace();
      return "❌ Fehler bei der Bildanalyse: " + e.getMessage();
    }
  }

  public String generateGptDescription(String marke, String produktart, String farbe, String material) {
    // Hier wird später OpenAI/GPT angebunden – aktuell Dummy-Text
    return String.format(
        "Die %s der Marke %s in der Farbe %s bestehen aus %s. " +
            "Sie bieten Komfort und Stil für den Alltag oder Sport.",
        produktart, marke, farbe, material);

  }
}
