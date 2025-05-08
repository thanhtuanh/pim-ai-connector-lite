package com.dnguyen.pes.controller;

import com.dnguyen.pes.dto.ProduktTypParameter;
import com.dnguyen.pes.service.ConfigService;
import com.dnguyen.pes.service.LanguageConfigService;
import com.dnguyen.pes.service.VisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/enrich")
public class EnrichmentController {

        private static final Logger logger = LoggerFactory.getLogger(EnrichmentController.class);
        private final VisionService visionService;
        private final ConfigService configService;
        private final LanguageConfigService languageConfigService;

        public EnrichmentController(VisionService visionService, ConfigService configService,
                        LanguageConfigService languageConfigService) {
                this.visionService = visionService;
                this.configService = configService;
                this.languageConfigService = languageConfigService;
        }

        @PostMapping("/gpt")
        public ResponseEntity<String> enrichWithGpt(@RequestBody ProduktTypParameter request) {
                return enrichWithGptLanguage(request, null);
        }

        // WICHTIG: Parameter explizit benennen mit name="language"
        @PostMapping("/gpt/{language}")
        public ResponseEntity<String> enrichWithGptLanguage(
                        @RequestBody ProduktTypParameter request,
                        @PathVariable(name = "language") String language) {

                try {
                        // Sprachcode ermitteln (falls angegeben)
                        String targetLanguage = language;
                        if (targetLanguage == null || targetLanguage.isEmpty()) {
                                targetLanguage = languageConfigService.getDefaultLanguage();
                        }

                        logger.info("GPT-Anfrage empfangen für Produkttyp: {} in Sprache: {}",
                                        configService.getProduktTypName(), targetLanguage);

                        // Parameter-Werte in eine Map umwandeln
                        Map<String, String> paramValues = new HashMap<>();
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

                        StringBuilder response = new StringBuilder();
                        response.append(heading).append(gptText);

                        return ResponseEntity.ok(response.toString());
                } catch (Exception e) {
                        logger.error("Fehler bei der GPT-Anfrage: " + e.getMessage(), e);

                        String errorMsg;
                        if (language != null && "en".equals(language.toLowerCase())) {
                                errorMsg = "❌ Error in GPT request: " + e.getMessage();
                        } else {
                                errorMsg = "❌ Fehler bei der GPT-Anfrage: " + e.getMessage();
                        }

                        return ResponseEntity.internalServerError().body(errorMsg);
                }
        }
}