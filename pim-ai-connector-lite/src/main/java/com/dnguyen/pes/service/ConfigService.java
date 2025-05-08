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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

@Service
public class ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    @Value("${ACTIVE_PRODUKTTYP_ID:fashion}")
    private String activeProduktTypId;

    private Document configDocument;
    private Map<String, String> parameterMap = new HashMap<>();
    private Map<String, String> demoDataMap = new HashMap<>();
    private String promptTemplate = "";
    private String produktTypName = "Standard";

    public ConfigService() {
        logger.info("ConfigService wird initialisiert");
    }

    @PostConstruct
    public void init() {
        try {
            logger.info("PostConstruct: Initialisiere Konfiguration mit Produkttyp-ID: {}",
                    activeProduktTypId != null ? activeProduktTypId : "null (wird Default 'fashion' verwenden)");

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

        // Standard-Demo-Daten
        demoDataMap = new HashMap<>();
        demoDataMap.put("p1", "Beispiel-Marke");
        demoDataMap.put("p2", "Beispiel-Produktart");
        demoDataMap.put("p3", "Beispiel-Farbe");
        demoDataMap.put("p4", "Beispiel-Material");

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

            produktTypName = getElementTextContent(activeProduktTyp, "n");
            logger.info("Produkttyp Name: {}", produktTypName);

            parameterMap = new HashMap<>();
            Element parameterElement = (Element) activeProduktTyp.getElementsByTagName("parameter").item(0);

            for (int i = 1; i <= 10; i++) {
                String paramName = "p" + i;
                String paramValue = getElementTextContent(parameterElement, paramName);
                parameterMap.put(paramName, paramValue);
                logger.debug("Parameter {}: {}", paramName, paramValue);
            }

            // Demo-Daten laden, falls vorhanden
            demoDataMap = new HashMap<>();
            Element demoElement = (Element) activeProduktTyp.getElementsByTagName("demo").item(0);
            if (demoElement != null) {
                for (int i = 1; i <= 10; i++) {
                    String paramName = "p" + i;
                    String demoValue = getElementTextContent(demoElement, paramName);
                    if (demoValue != null && !demoValue.isEmpty()) {
                        demoDataMap.put(paramName, demoValue);
                        logger.debug("Demo-Wert für {}: {}", paramName, demoValue);
                    }
                }
            }

            promptTemplate = getElementTextContent(activeProduktTyp, "prompt-template");

            logger.info("Produkttyp '{}' erfolgreich geladen", produktTypName);

        } catch (Exception e) {
            logger.error("Fehler beim Laden des aktiven Produkttyps: " + e.getMessage(), e);
            setupDefaultValues();
        }
    }

    public Map<String, Object> loadProduktTypById(String produktTypId) {
        try {
            if (configDocument == null) {
                loadConfiguration();
            }

            NodeList produktTypList = configDocument.getElementsByTagName("produkttyp");

            for (int i = 0; i < produktTypList.getLength(); i++) {
                Element produktTyp = (Element) produktTypList.item(i);
                String id = produktTyp.getAttribute("id");

                if (id.equals(produktTypId)) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", id);
                    result.put("name", getElementTextContent(produktTyp, "n"));

                    Map<String, String> parameter = new HashMap<>();
                    Element parameterElement = (Element) produktTyp.getElementsByTagName("parameter").item(0);

                    if (parameterElement != null) {
                        for (int j = 1; j <= 10; j++) {
                            String paramName = "p" + j;
                            String paramValue = getElementTextContent(parameterElement, paramName);
                            if (paramValue != null && !paramValue.isEmpty()) {
                                parameter.put(paramName, paramValue);
                            }
                        }
                    }

                    result.put("parameter", parameter);
                    result.put("promptTemplate", getElementTextContent(produktTyp, "prompt-template"));

                    // Demo-Daten hinzufügen, falls vorhanden
                    Map<String, String> demoData = new HashMap<>();
                    Element demoElement = (Element) produktTyp.getElementsByTagName("demo").item(0);
                    if (demoElement != null) {
                        for (int j = 1; j <= 10; j++) {
                            String paramName = "p" + j;
                            String demoValue = getElementTextContent(demoElement, paramName);
                            if (demoValue != null && !demoValue.isEmpty()) {
                                demoData.put(paramName, demoValue);
                            }
                        }
                        result.put("demoData", demoData);
                    }

                    return result;
                }
            }

            logger.warn("Produkttyp mit ID '{}' nicht gefunden", produktTypId);
            return null;

        } catch (Exception e) {
            logger.error("Fehler beim Laden des Produkttyps {}: {}", produktTypId, e.getMessage());
            return null;
        }
    }

    public List<Map<String, String>> getAllProduktTypen() {
        List<Map<String, String>> result = new ArrayList<>();

        try {
            if (configDocument == null) {
                loadConfiguration();
            }

            NodeList produktTypList = configDocument.getElementsByTagName("produkttyp");

            for (int i = 0; i < produktTypList.getLength(); i++) {
                Element produktTyp = (Element) produktTypList.item(i);
                String id = produktTyp.getAttribute("id");
                String name = getElementTextContent(produktTyp, "n");

                Map<String, String> produktTypInfo = new HashMap<>();
                produktTypInfo.put("id", id);
                produktTypInfo.put("name", name);

                result.add(produktTypInfo);
            }

        } catch (Exception e) {
            logger.error("Fehler beim Laden aller Produkttypen: {}", e.getMessage());

            // Fallback-Produkttypen hinzufügen
            Map<String, String> defaultTyp1 = new HashMap<>();
            defaultTyp1.put("id", "fashion");
            defaultTyp1.put("name", "Bekleidung");
            result.add(defaultTyp1);

            Map<String, String> defaultTyp2 = new HashMap<>();
            defaultTyp2.put("id", "electronics");
            defaultTyp2.put("name", "Elektronik");
            result.add(defaultTyp2);

            Map<String, String> defaultTyp3 = new HashMap<>();
            defaultTyp3.put("id", "furniture");
            defaultTyp3.put("name", "Möbel");
            result.add(defaultTyp3);

            Map<String, String> defaultTyp4 = new HashMap<>();
            defaultTyp4.put("id", "books");
            defaultTyp4.put("name", "Bücher");
            result.add(defaultTyp4);
        }

        return result;
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

    public Map<String, String> getDemoDataMap() {
        return demoDataMap;
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

    public boolean changeActiveProduktTyp(String produktTypId) {
        try {
            if (configDocument == null) {
                loadConfiguration();
            }

            NodeList produktTypList = configDocument.getElementsByTagName("produkttyp");

            for (int i = 0; i < produktTypList.getLength(); i++) {
                Element produktTyp = (Element) produktTypList.item(i);
                String id = produktTyp.getAttribute("id");

                if (id.equals(produktTypId)) {
                    activeProduktTypId = produktTypId;
                    loadActiveProduktTyp();
                    return true;
                }
            }

            logger.warn("Produkttyp mit ID '{}' nicht gefunden, aktiver Typ bleibt '{}'",
                    produktTypId, activeProduktTypId);
            return false;

        } catch (Exception e) {
            logger.error("Fehler beim Ändern des aktiven Produkttyps: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Gibt die XML-Konfiguration als String zurück
     */
    public String getXMLConfigAsString() throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource("pim-config.xml");
            if (!resource.exists()) {
                throw new Exception("pim-config.xml wurde nicht gefunden!");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();
                return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logger.error("Fehler beim Lesen der XML-Konfiguration: " + e.getMessage(), e);
            throw e;
        }
    }
}