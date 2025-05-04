package com.dnguyen.pes.controller;

import com.dnguyen.pes.service.VisionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/vision")
@RequiredArgsConstructor
public class VisionController {

    private final VisionService visionService;

    @Value("${google.vision.api-key}")
    private String apiKey;

    @PostMapping("/analyzeAndText")
    public ResponseEntity<String> analyzeImageWithText(@RequestBody Map<String, String> request) {
        try {
            String base64Image = request.get("imageBase64");
            if (base64Image == null || base64Image.isEmpty()) {
                return ResponseEntity.badRequest().body("⚠️ Kein Bild übermittelt.");
            }

            // Base64 säubern
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }
            base64Image = base64Image.replaceAll("[^A-Za-z0-9+/=]", "");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Aufruf Vision API
            String visionResult = visionService.analyzeImage(imageBytes);

            // GPT Prompt vorbereiten
            String marke = request.getOrDefault("marke", "");
            String produktart = request.getOrDefault("produktart", "");
            String farbe = request.getOrDefault("farbe", "");
            String material = request.getOrDefault("material", "");

            String gptText = visionService.generateGptDescription(marke, produktart, farbe, material);

            // Antwort kombinieren
            StringBuilder response = new StringBuilder();
            response.append("=== Google Vision Ergebnis ===\n")
                    .append(visionResult).append("\n\n")
                    .append("=== GPT Produktbeschreibung ===\n")
                    .append(gptText);

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("❌ Fehler bei der Verarbeitung: " + e.getMessage());
        }
    }
}
