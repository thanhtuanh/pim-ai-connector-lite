package com.dnguyen.pes.controller;

import com.dnguyen.pes.dto.ProduktTypParameter;
import com.dnguyen.pes.service.ConfigService;
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

    public VisionController(VisionService visionService, ConfigService configService) {
        this.visionService = visionService;
        this.configService = configService;
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        logger.info("Konfiguration angefordert");
        Map<String, Object> config = new HashMap<>();
        config.put("produktTypId", configService.getActiveProduktTypId());
        config.put("produktTypName", configService.getProduktTypName());
        config.put("parameter", configService.getParameterMap());
        return ResponseEntity.ok(config);
    }

    @PostMapping("/analyzeAndText")
    public ResponseEntity<String> analyzeImageWithText(@RequestBody ProduktTypParameter request) {
        try {
            logger.info("Bild- und Text-Analyse angefordert");
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

                response.append("=== Google Vision Ergebnis ===\n")
                        .append(visionResult).append("\n\n");
            } else {
                logger.info("Keine Bildanalyse - nur Text-Generierung");
            }

            // GPT-Beschreibung generieren
            String gptText = visionService.generateGptDescription(paramValues);
            response.append("=== Produktbeschreibung (" + configService.getProduktTypName() + ") ===\n")
                    .append(gptText);

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            logger.error("Fehler bei der Verarbeitung: " + e.getMessage(), e);
            return ResponseEntity.internalServerError().body("❌ Fehler bei der Verarbeitung: " + e.getMessage());
        }
    }
}