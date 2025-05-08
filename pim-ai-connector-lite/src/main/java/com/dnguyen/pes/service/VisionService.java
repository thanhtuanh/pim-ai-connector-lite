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
  private final LanguageConfigService languageConfigService;

  public VisionService(ConfigService configService, LanguageConfigService languageConfigService) {
    this.configService = configService;
    this.languageConfigService = languageConfigService;
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
        return getLocalizedMessage("no_vision_response");
      }

      Map<String, Object> annotation = responses.get(0);
      Map<String, Object> textAnnotation = (Map<String, Object>) annotation.get("fullTextAnnotation");
      if (textAnnotation == null || !textAnnotation.containsKey("text")) {
        return getLocalizedMessage("no_text_detected");
      }

      return textAnnotation.get("text").toString().trim();

    } catch (Exception e) {
      logger.error("Fehler bei der Bildanalyse: " + e.getMessage(), e);
      return getLocalizedMessage("image_analysis_error") + " " + e.getMessage();
    }
  }

  public String generateGptDescription(Map<String, String> paramValues) {
    return generateGptDescription(paramValues, null);
  }

  public String generateGptDescription(Map<String, String> paramValues, String language) {
    try {
      // Sprache bestimmen
      String targetLanguage = (language != null && !language.isEmpty()) ? language
          : languageConfigService.getDefaultLanguage();

      // GPT-Prompt aus der Konfiguration generieren
      String prompt = configService.generatePrompt(paramValues);

      // Sprachspezifischen Suffix hinzufügen
      prompt += "\n\n" + languageConfigService.getPromptSuffix(targetLanguage);

      logger.info("Generiere GPT-Beschreibung in Sprache '{}' mit Prompt: {}", targetLanguage, prompt);

      // Systemnachricht für die gewählte Sprache laden
      String systemMessage = languageConfigService.getSystemMessage(targetLanguage);

      // API-Anfrage erstellen
      Map<String, Object> requestBody = Map.of(
          "model", "gpt-3.5-turbo",
          "messages", List.of(
              Map.of("role", "system", "content", systemMessage),
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

      return getLocalizedMessage("no_gpt_response", targetLanguage);
    } catch (Exception e) {
      logger.error("Fehler bei der GPT-Anfrage: " + e.getMessage(), e);
      return getLocalizedMessage("gpt_error", language) + " " + e.getMessage();
    }
  }

  /**
   * Gibt eine lokalisierte Fehlermeldung zurück
   */
  private String getLocalizedMessage(String messageKey) {
    return getLocalizedMessage(messageKey, languageConfigService.getDefaultLanguage());
  }

  /**
   * Gibt eine lokalisierte Fehlermeldung in der angegebenen Sprache zurück
   */
  private String getLocalizedMessage(String messageKey, String language) {
    Map<String, Map<String, String>> messages = new HashMap<>();

    // Deutsche Fehlermeldungen
    Map<String, String> deMessages = new HashMap<>();
    deMessages.put("no_vision_response", "⚠️ Keine Antwort von Google Vision erhalten.");
    deMessages.put("no_text_detected", "⚠️ Kein Text im Bild erkannt.");
    deMessages.put("image_analysis_error", "❌ Fehler bei der Bildanalyse:");
    deMessages.put("no_gpt_response", "⚠️ Keine Antwort von GPT erhalten.");
    deMessages.put("gpt_error", "❌ Fehler bei der GPT-Anfrage:");

    // Englische Fehlermeldungen
    Map<String, String> enMessages = new HashMap<>();
    enMessages.put("no_vision_response", "⚠️ No response received from Google Vision.");
    enMessages.put("no_text_detected", "⚠️ No text detected in the image.");
    enMessages.put("image_analysis_error", "❌ Error during image analysis:");
    enMessages.put("no_gpt_response", "⚠️ No response received from GPT.");
    enMessages.put("gpt_error", "❌ Error in GPT request:");

    messages.put("de", deMessages);
    messages.put("en", enMessages);

    // Sprache bestimmen
    String normalizedLanguage = (language != null) ? language.toLowerCase() : "de";
    if (!messages.containsKey(normalizedLanguage)) {
      normalizedLanguage = "de"; // Fallback auf Deutsch
    }

    // Nachricht zurückgeben oder Fallback wenn nicht gefunden
    return messages.get(normalizedLanguage).getOrDefault(messageKey, messageKey);
  }
}