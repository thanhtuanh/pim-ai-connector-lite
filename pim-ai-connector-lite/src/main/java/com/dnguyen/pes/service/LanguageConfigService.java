package com.dnguyen.pes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Service zur Verwaltung der Sprachkonfiguration für OpenAI-API-Anfragen.
 * Ermöglicht das Umschalten zwischen verschiedenen Sprachen (DE, EN) für
 * Produktbeschreibungen.
 */
@Service
public class LanguageConfigService {

    private static final Logger logger = LoggerFactory.getLogger(LanguageConfigService.class);

    // Standardwert 'de' wird verwendet, wenn keine Umgebungsvariable gesetzt ist
    @Value("${API_LANGUAGE:de}")
    private String defaultLanguage;

    // Map für Sprach-spezifische Systemnachrichten
    private final Map<String, String> languageSystemMessages = new HashMap<>();

    // Map für Sprach-spezifische Prompt-Suffixe
    private final Map<String, String> languagePromptSuffixes = new HashMap<>();

    @PostConstruct
    public void init() {
        logger.info("Initialisiere Sprachkonfiguration mit Standardsprache: {}", defaultLanguage);

        // Initialisiere Systemnachrichten für verschiedene Sprachen
        languageSystemMessages.put("de",
                "Du bist ein Produktbeschreibungs-Assistent. Deine Aufgabe ist es, ansprechende und verkaufsfördernde Beschreibungen zu erstellen. Antworte immer auf Deutsch, unabhängig von der Eingabesprache.");
        languageSystemMessages.put("en",
                "You are a product description assistant. Your task is to create appealing and sales-promoting descriptions. Always respond in English, regardless of the input language.");

        // Initialisiere Prompt-Suffixe für verschiedene Sprachen
        languagePromptSuffixes.put("de",
                "Erstelle eine verkaufsfördernde Beschreibung in deutscher Sprache. Verwende präzise deutsche Begriffe und eine ansprechende Sprache.");
        languagePromptSuffixes.put("en",
                "Create a sales-promoting description in English language. Use precise English terms and appealing language.");

        // Validiere die Standardsprache
        if (!languageSystemMessages.containsKey(defaultLanguage)) {
            logger.warn("Unbekannte Standardsprache '{}'. Verwende 'de' als Fallback.", defaultLanguage);
            defaultLanguage = "de";
        }
    }

    /**
     * Liefert die aktuelle Standardsprache zurück
     *
     * @return Sprachcode (de, en)
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Ändert die Standardsprache zur Laufzeit
     *
     * @param language Neuer Sprachcode (de, en)
     * @return true wenn erfolgreich, false wenn Sprache nicht unterstützt wird
     */
    public boolean setDefaultLanguage(String language) {
        if (languageSystemMessages.containsKey(language)) {
            defaultLanguage = language;
            logger.info("Standardsprache geändert auf: {}", language);
            return true;
        } else {
            logger.warn("Sprache '{}' wird nicht unterstützt.", language);
            return false;
        }
    }

    /**
     * Gibt die Systemnachricht für die angegebene Sprache zurück
     *
     * @param language Sprachcode (de, en)
     * @return Systemnachricht für OpenAI-API
     */
    public String getSystemMessage(String language) {
        String normalizedLanguage = normalizeLanguageCode(language);
        return languageSystemMessages.getOrDefault(normalizedLanguage,
                languageSystemMessages.get(defaultLanguage));
    }

    /**
     * Gibt das Prompt-Suffix für die angegebene Sprache zurück
     *
     * @param language Sprachcode (de, en)
     * @return Prompt-Suffix zur Anpassung des GPT-Prompts
     */
    public String getPromptSuffix(String language) {
        String normalizedLanguage = normalizeLanguageCode(language);
        return languagePromptSuffixes.getOrDefault(normalizedLanguage,
                languagePromptSuffixes.get(defaultLanguage));
    }

    /**
     * Prüft, ob eine Sprache unterstützt wird
     *
     * @param language Sprachcode (de, en)
     * @return true wenn unterstützt, sonst false
     */
    public boolean isLanguageSupported(String language) {
        return languageSystemMessages.containsKey(normalizeLanguageCode(language));
    }

    /**
     * Normalisiert einen Sprachcode (Kleinbuchstaben, Trimmen)
     *
     * @param language Sprachcode (DE, en, En, etc.)
     * @return Normalisierter Sprachcode (de, en)
     */
    private String normalizeLanguageCode(String language) {
        if (language == null || language.isEmpty()) {
            return defaultLanguage;
        }
        return language.trim().toLowerCase();
    }

    /**
     * Gibt alle unterstützten Sprachen zurück
     *
     * @return Map mit Sprachcode als Schlüssel und lokalisierten Sprachnamen
     */
    public Map<String, String> getSupportedLanguages() {
        Map<String, String> languages = new HashMap<>();
        languages.put("de", "Deutsch");
        languages.put("en", "English");
        return languages;
    }
}