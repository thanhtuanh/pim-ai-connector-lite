# Sprachkonfiguration im PIM-AI-Connector-Lite

Diese Dokumentation beschreibt die neue Sprachkonfiguration im PIM-AI-Connector-Lite, mit der Produktbeschreibungen in verschiedenen Sprachen (aktuell Deutsch und Englisch) generiert werden können.

## Überblick

Die Sprachkonfiguration ermöglicht es, die Ausgabesprache für OpenAI-API-Anfragen zu steuern. Dies geschieht durch:

1. **System-Nachrichten**: Spezifische Anweisungen an das OpenAI-Modell, in einer bestimmten Sprache zu antworten
2. **Prompt-Suffixe**: Sprachspezifische Ergänzungen der Prompts
3. **Dynamische API-Endpunkte**: Möglichkeit, die Sprache per API-Aufruf zu wechseln

## Konfiguration

### Umgebungsvariablen

Die Standardsprache kann über die Umgebungsvariable `API_LANGUAGE` gesetzt werden:

```
# In der .env-Datei oder als Umgebungsvariable
API_LANGUAGE=de  # Deutsch als Standard
API_LANGUAGE=en  # Englisch als Standard
```

### Docker-Konfiguration

Bei Verwendung von Docker:

```bash
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your_api_key \
  -e API_LANGUAGE=de \
  -e ACTIVE_PRODUKTTYP_ID=fashion \
  dnguyen/pim-ai-connector-lite
```

## API-Verwendung

### Standardsprache abrufen

```
GET /api/language/current
```

Antwort:
```json
{
  "language": "de",
  "name": "Deutsch"
}
```

### Standardsprache ändern

```
POST /api/language/set?language=en
```

Antwort:
```json
{
  "success": true,
  "language": "en",
  "name": "English",
  "message": "Sprache erfolgreich geändert auf: English"
}
```

### Unterstützte Sprachen abrufen

```
GET /api/language/supported
```

Antwort:
```json
{
  "current": "de",
  "languages": {
    "de": "Deutsch",
    "en": "English"
  }
}
```

### Produktbeschreibung in bestimmter Sprache generieren

Standardsprache (wie in der Konfiguration festgelegt):
```
POST /api/enrich/gpt
```

Spezifische Sprache:
```
POST /api/enrich/gpt/en  # Englisch
POST /api/enrich/gpt/de  # Deutsch
```

Body:
```json
{
  "p1": "Nike",
  "p2": "Laufschuhe",
  "p3": "Schwarz mit roten Akzenten",
  "p4": "Synthetik und Mesh",
  "p5": "EU 42"
}
```

## Integration in anderen Code

Die Sprachkonfiguration kann einfach in anderen Klassen verwendet werden:

```java
@Autowired
private LanguageConfigService languageConfigService;

// Aktuelle Standardsprache abrufen
String currentLanguage = languageConfigService.getDefaultLanguage();

// Systemnachricht für eine bestimmte Sprache abrufen
String systemMessage = languageConfigService.getSystemMessage("de");

// Prüfen, ob eine Sprache unterstützt wird
boolean isSupported = languageConfigService.isLanguageSupported("fr");
```

## Erweiterung um zusätzliche Sprachen

Um weitere Sprachen zu unterstützen, müssen folgende Schritte durchgeführt werden:

1. In der Klasse `LanguageConfigService` neue Einträge für die gewünschte Sprache hinzufügen:
   ```java
   // Initialisiere Systemnachrichten
   languageSystemMessages.put("fr", "Vous êtes un assistant de description de produit...");
   
   // Initialisiere Prompt-Suffixe
   languagePromptSuffixes.put("fr", "Créez une description en français...");
   
   // Unterstützte Sprachen
   languages.put("fr", "Français");
   ```

2. In der Klasse `VisionService` lokalisierte Fehlermeldungen für die neue Sprache hinzufügen.

## Anpassung der Systemnachrichten

Die Systemnachrichten können in der Klasse `LanguageConfigService` angepasst werden, um den Stil und Ton der generierten Beschreibungen zu beeinflussen.