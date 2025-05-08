package com.dnguyen.pes.controller;

import com.dnguyen.pes.dto.ProduktTypParameter;
import com.dnguyen.pes.service.ConfigService;
import com.dnguyen.pes.service.LanguageConfigService;
import com.dnguyen.pes.service.VisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vision")
public class VisionController {

    private static final Logger logger = LoggerFactory.getLogger(VisionController.class);
    private final VisionService visionService;
    private final ConfigService configService;
    private final LanguageConfigService languageConfigService;

    public VisionController(VisionService visionService, ConfigService configService,
            LanguageConfigService languageConfigService) {
        this.visionService = visionService;
        this.configService = configService;
        this.languageConfigService = languageConfigService;
    }

    // WICHTIG: Parameter explizit benennen mit name="type"
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig(@RequestParam(name = "type", required = false) String type) {
        logger.info("Konfiguration angefordert für Typ: {}", type != null ? type : "aktiv");

        Map<String, Object> config;

        // Wenn ein spezifischer Produkttyp angefordert wurde
        if (type != null && !type.isEmpty()) {
            config = configService.loadProduktTypById(type);

            // Fallback auf aktiven Produkttyp, wenn angeforderten nicht gefunden
            if (config == null) {
                logger.warn("Angeforderten Produkttyp '{}' nicht gefunden, gebe aktiven Produkttyp zurück", type);
                config = new HashMap<>();
                config.put("produktTypId", configService.getActiveProduktTypId());
                config.put("produktTypName", configService.getProduktTypName());
                config.put("parameter", configService.getParameterMap());
                // Demo-Daten hinzufügen
                config.put("demoData", configService.getDemoDataMap());
            }
        } else {
            // Standard-Konfiguration zurückgeben (aktiver Produkttyp)
            config = new HashMap<>();
            config.put("produktTypId", configService.getActiveProduktTypId());
            config.put("produktTypName", configService.getProduktTypName());
            config.put("parameter", configService.getParameterMap());
            // Demo-Daten hinzufügen
            config.put("demoData", configService.getDemoDataMap());
        }

        return ResponseEntity.ok(config);
    }

    // NEU: Endpunkt zum direkten Abrufen der XML-Konfiguration
    @GetMapping("/xml-config")
    public ResponseEntity<String> getXMLConfig() {
        try {
            // XML-Konfiguration aus der ConfigService-Klasse abrufen
            String xmlConfig = configService.getXMLConfigAsString();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/xml")
                    .body(xmlConfig);
        } catch (Exception e) {
            logger.error("Fehler beim Abrufen der XML-Konfiguration: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/allProductTypes")
    public ResponseEntity<Map<String, Object>> getAllProductTypes() {
        logger.info("Alle Produkttypen angefordert");

        Map<String, Object> response = new HashMap<>();
        response.put("activeType", configService.getActiveProduktTypId());
        response.put("produktTypen", configService.getAllProduktTypen());

        return ResponseEntity.ok(response);
    }

    // WICHTIG: Parameter explizit benennen mit name="type"
    @PostMapping("/setProductType")
    public ResponseEntity<Map<String, Object>> setProductType(@RequestParam(name = "type") String type) {
        logger.info("Produkttyp-Änderung angefordert: {}", type);

        Map<String, Object> response = new HashMap<>();
        boolean success = configService.changeActiveProduktTyp(type);

        if (success) {
            response.put("success", true);
            response.put("message", "Produkttyp erfolgreich geändert auf: " + configService.getProduktTypName());
            response.put("produktTypId", configService.getActiveProduktTypId());
            response.put("produktTypName", configService.getProduktTypName());
            response.put("parameter", configService.getParameterMap());
            // Demo-Daten hinzufügen
            response.put("demoData", configService.getDemoDataMap());
        } else {
            response.put("success", false);
            response.put("message", "Produkttyp '" + type + "' nicht gefunden");
            response.put("availableTypes", configService.getAllProduktTypen());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/analyzeAndText")
    public ResponseEntity<String> analyzeImageWithText(@RequestBody ProduktTypParameter request) {
        return analyzeImageWithTextLanguage(request, null);
    }

    // WICHTIG: Parameter explizit benennen mit name="language"
    @PostMapping("/analyzeAndText/{language}")
    public ResponseEntity<String> analyzeImageWithTextLanguage(
            @RequestBody ProduktTypParameter request,
            @PathVariable(name = "language") String language) {

        try {
            // Sprachcode ermitteln (falls angegeben)
            String targetLanguage = language;
            if (targetLanguage == null || targetLanguage.isEmpty()) {
                targetLanguage = languageConfigService.getDefaultLanguage();
            }

            logger.info("Bild- und Text-Analyse angefordert in Sprache: {}", targetLanguage);
            StringBuilder response = new StringBuilder();
            Map<String, String> paramValues = new HashMap<>();

            // Parameter-Werte in eine Map umwandeln
            if (request.getP1() != null)
                paramValues.put("p1", request.getP1());
            if (request.getP2() != null)
                paramValues.put("p2", request.getP2());
            if (request.getP3() != null)
                paramValues.put("p3", request.getP3());
            if (request.getP4() != null)
                paramValues.put("p4", request.getP4());
            if (request.getP5() != null)
                paramValues.put("p5", request.getP5());
            if (request.getP6() != null)
                paramValues.put("p6", request.getP6());
            if (request.getP7() != null)
                paramValues.put("p7", request.getP7());
            if (request.getP8() != null)
                paramValues.put("p8", request.getP8());
            if (request.getP9() != null)
                paramValues.put("p9", request.getP9());
            if (request.getP10() != null)
                paramValues.put("p10", request.getP10());

            // Wenn Bild vorhanden ist, Vision API aufrufen
            String base64Image = request.getImageBase64();
            if (base64Image != null && !base64Image.isEmpty()) {
                logger.info("Bildanalyse wird durchgeführt");
                // Base64 säubern
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                }
                base64Image = base64Image.replaceAll("[^A-Za-z0-9+/=]", "");

                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                // Aufruf Vision API
                String visionResult = visionService.analyzeImage(imageBytes);

                // Überschrift lokalisieren
                String heading;
                if ("en".equals(targetLanguage)) {
                    heading = "=== Google Vision Result ===\n";
                } else {
                    heading = "=== Google Vision Ergebnis ===\n";
                }

                response.append(heading)
                        .append(visionResult).append("\n\n");
            } else {
                logger.info("Keine Bildanalyse - nur Text-Generierung");
            }

            // GPT-Beschreibung in der gewünschten Sprache generieren
            String gptText = visionService.generateGptDescription(paramValues, targetLanguage);

            // Produkttyp-Name holen
            String produktTypName = configService.getProduktTypName();

            // Überschrift lokalisieren
            String heading;
            if ("en".equals(targetLanguage)) {
                heading = "=== Product Description (" + produktTypName + ") ===\n";
            } else {
                heading = "=== Produktbeschreibung (" + produktTypName + ") ===\n";
            }

            response.append(heading).append(gptText);

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            logger.error("Fehler bei der Verarbeitung: " + e.getMessage(), e);

            String errorMsg;
            if (language != null && "en".equals(language.toLowerCase())) {
                errorMsg = "❌ Error during processing: " + e.getMessage();
            } else {
                errorMsg = "❌ Fehler bei der Verarbeitung: " + e.getMessage();
            }

            return ResponseEntity.internalServerError().body(errorMsg);
        }
    }
}