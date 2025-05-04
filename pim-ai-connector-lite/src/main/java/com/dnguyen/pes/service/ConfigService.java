package com.dnguyen.pes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

@Service
public class ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    // Standardwert "fashion" wird verwendet, wenn keine Umgebungsvariable gesetzt
    // ist
    @Value("${ACTIVE_PRODUKTTYP_ID:fashion}")
    private String activeProduktTypId;

    private Document configDocument;
    private Map<String, String> parameterMap = new HashMap<>();
    private String promptTemplate = "";
    private String produktTypName = "Standard";

    public ConfigService() {
        // Leerer Konstruktor - Initialisierung erfolgt in init()
        logger.info("ConfigService wird initialisiert");
    }

    @PostConstruct
    public void init() {
        try {
            logger.info("PostConstruct: Initialisiere Konfiguration mit Produkttyp-ID: {}",
                    activeProduktTypId != null ? activeProduktTypId : "null (wird Default 'fashion' verwenden)");

            // Sicherheitscheck für null-Wert
            if (activeProduktTypId == null) {
                activeProduktTypId = "fashion";
                logger.warn("Aktiver Produkttyp ist null, setze auf Standard-Wert 'fashion'");
            }

            loadConfiguration();
        } catch (Exception e) {
            logger.error("Fehler bei der Initialisierung: " + e.getMessage(), e);
            setupDefaultValues();
        }
    }

    private void setupDefaultValues() {
        logger.info("Verwende Standard-Konfigurationswerte");
        produktTypName = "Standard-Produkttyp";
        parameterMap = new HashMap<>();
        parameterMap.put("p1", "Marke");
        parameterMap.put("p2", "Produktart");
        parameterMap.put("p3", "Farbe");
        parameterMap.put("p4", "Material");

        promptTemplate = "Erstelle eine Produktbeschreibung für folgendes Produkt:\n" +
                "- Marke: {p1}\n" +
                "- Produktart: {p2}\n" +
                "- Farbe: {p3}\n" +
                "- Material: {p4}";
    }

    private void loadConfiguration() {
        try {
            logger.info("Lade Konfiguration für Produkttyp: {}", activeProduktTypId);

            ClassPathResource resource = new ClassPathResource("pim-config.xml");
            if (!resource.exists()) {
                logger.error("pim-config.xml wurde nicht gefunden! Verwende Standard-Werte.");
                setupDefaultValues();
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            try (InputStream inputStream = resource.getInputStream()) {
                configDocument = builder.parse(inputStream);
                loadActiveProduktTyp();
                logger.info("Konfiguration erfolgreich geladen");
            }
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Konfiguration: " + e.getMessage(), e);
            setupDefaultValues();
        }
    }

    private void loadActiveProduktTyp() {
        try {
            NodeList produktTypList = configDocument.getElementsByTagName("produkttyp");
            logger.info("Gefundene Produkttypen: {}", produktTypList.getLength());

            Element activeProduktTyp = null;

            for (int i = 0; i < produktTypList.getLength(); i++) {
                Element produktTyp = (Element) produktTypList.item(i);
                String id = produktTyp.getAttribute("id");
                logger.info("Prüfe Produkttyp mit ID: {}", id);

                if (id.equals(activeProduktTypId)) {
                    activeProduktTyp = produktTyp;
                    break;
                }
            }

            if (activeProduktTyp == null) {
                logger.warn("Produkttyp mit ID '{}' nicht gefunden. Verwende ersten verfügbaren Produkttyp.",
                        activeProduktTypId);

                // Wenn der konfigurierte Produkttyp nicht gefunden wurde, verwende den ersten
                // verfügbaren
                if (produktTypList.getLength() > 0) {
                    activeProduktTyp = (Element) produktTypList.item(0);
                    activeProduktTypId = activeProduktTyp.getAttribute("id");
                    logger.info("Verwende Produkttyp mit ID: {}", activeProduktTypId);
                } else {
                    logger.error("Keine Produkttypen in der Konfiguration gefunden!");
                    setupDefaultValues();
                    return;
                }
            }

            // Produkttyp Name laden
            produktTypName = getElementTextContent(activeProduktTyp, "n");
            logger.info("Produkttyp Name: {}", produktTypName);

            // Parameter laden
            parameterMap = new HashMap<>();
            Element parameterElement = (Element) activeProduktTyp.getElementsByTagName("parameter").item(0);

            for (int i = 1; i <= 10; i++) {
                String paramName = "p" + i;
                String paramValue = getElementTextContent(parameterElement, paramName);
                parameterMap.put(paramName, paramValue);
                logger.debug("Parameter {}: {}", paramName, paramValue);
            }

            // Prompt-Template laden
            promptTemplate = getElementTextContent(activeProduktTyp, "prompt-template");

            logger.info("Produkttyp '{}' erfolgreich geladen", produktTypName);

        } catch (Exception e) {
            logger.error("Fehler beim Laden des aktiven Produkttyps: " + e.getMessage(), e);
            setupDefaultValues();
        }
    }

    private String getElementTextContent(Element parent, String tagName) {
        NodeList elementList = parent.getElementsByTagName(tagName);
        if (elementList.getLength() > 0) {
            return elementList.item(0).getTextContent().trim();
        }
        return "";
    }

    public String getActiveProduktTypId() {
        return activeProduktTypId;
    }

    public String getProduktTypName() {
        return produktTypName;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public String getParameterName(String paramKey) {
        return parameterMap.getOrDefault(paramKey, paramKey);
    }

    public String getPromptTemplate() {
        return promptTemplate;
    }

    public String generatePrompt(Map<String, String> paramValues) {
        String prompt = promptTemplate;

        for (Map.Entry<String, String> entry : paramValues.entrySet()) {
            prompt = prompt.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return prompt;
    }

    public void refreshConfiguration() {
        loadConfiguration();
    }
}