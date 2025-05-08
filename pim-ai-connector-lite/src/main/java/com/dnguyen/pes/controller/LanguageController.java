package com.dnguyen.pes.controller;

import com.dnguyen.pes.service.LanguageConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

    private static final Logger logger = LoggerFactory.getLogger(LanguageController.class);
    private final LanguageConfigService languageConfigService;

    public LanguageController(LanguageConfigService languageConfigService) {
        this.languageConfigService = languageConfigService;
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, String>> getCurrentLanguage() {
        String currentLanguage = languageConfigService.getDefaultLanguage();
        logger.info("Aktuelle Sprache abgefragt: {}", currentLanguage);

        Map<String, String> response = new HashMap<>();
        response.put("language", currentLanguage);
        response.put("name", getCurrentLanguageName(currentLanguage));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/set")
    public ResponseEntity<Map<String, Object>> setLanguage(@RequestParam(name = "language") String language) {
        logger.info("Sprachänderungsanfrage: {}", language);

        Map<String, Object> response = new HashMap<>();
        boolean success = languageConfigService.setDefaultLanguage(language);

        if (success) {
            response.put("success", true);
            response.put("language", language);
            response.put("name", getCurrentLanguageName(language));
            response.put("message", "Sprache erfolgreich geändert auf: " + getCurrentLanguageName(language));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Sprache '" + language + "' wird nicht unterstützt");
            response.put("supportedLanguages", languageConfigService.getSupportedLanguages());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/supported")
    public ResponseEntity<Map<String, Object>> getSupportedLanguages() {
        logger.info("Unterstützte Sprachen abgefragt");

        Map<String, Object> response = new HashMap<>();
        response.put("current", languageConfigService.getDefaultLanguage());
        response.put("languages", languageConfigService.getSupportedLanguages());

        return ResponseEntity.ok(response);
    }

    private String getCurrentLanguageName(String languageCode) {
        return languageConfigService.getSupportedLanguages().getOrDefault(languageCode, languageCode);
    }
}