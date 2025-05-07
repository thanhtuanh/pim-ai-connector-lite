# PIM-AI-Connector-Lite

Ein KI-basierter Produktdaten-Service zur automatischen Erstellung von hochwertigen, SEO-optimierten Produktbeschreibungen und Bildanalysen.

## Übersicht

Der PIM-AI-Connector-Lite ist eine leistungsstarke Middleware, die strukturierte Produktdaten, bestehende Beschreibungen und Produktbilder verarbeitet, analysiert und in hochwertige, marketingfertige Inhalte umwandelt. Die Anwendung nutzt OpenAI's GPT und Google Vision API, um intelligente Produktbeschreibungen zu generieren und Bilddaten zu analysieren.

### Hauptfunktionen

- **KI-generierte Produktbeschreibungen**: Automatische Erstellung verkaufsfördernder Texte basierend auf strukturierten Produktdaten
- **Bildanalyse**: Integration mit Google Vision API zur Extraktion von Textinformationen aus Produktbildern
- **Mehrsprachige Unterstützung**: Dynamische Generierung von Inhalten in Deutsch, Englisch und erweiterbar für andere Sprachen
- **Konfigurierbare Produkttypen**: Verschiedene vordefinierte Produktkategorien (Bekleidung, Elektronik, Möbel) mit spezifischen Parametern
- **Responsive Web-Interface**: Benutzerfreundliche Demo-Oberfläche zum Testen der KI-Funktionen

## Technologien

- **Backend**: Java 17, Spring Boot 3.2.4
- **APIs**: OpenAI GPT, Google Vision API
- **Container**: Docker, Docker Compose
- **Frontend**: HTML, CSS, JavaScript (Vanilla JS)
- **Konfiguration**: XML-basierte Produkttyp-Definition

## Projektstruktur

```
pim-ai-connector-lite/
├── src/
│   ├── main/
│   │   ├── java/com/dnguyen/pes/
│   │   │   ├── controller/        # REST-Endpunkte
│   │   │   ├── dto/               # Datenübertragungsobjekte
│   │   │   ├── service/           # Geschäftslogik
│   │   │   └── ProductEnrichmentServiceApplication.java
│   │   └── resources/
│   │       ├── static/            # Web-Interface
│   │       ├── application.properties
│   │       └── pim-config.xml     # Produkttyp-Konfiguration
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Wichtige Hinweise zur Kompilierung

**Kritisch:** Bei der Kompilierung muss der Maven-Parameter `-parameters` aktiviert sein, damit Spring Boot die Methodenparameter korrekt erkennen kann. Dies ist besonders wichtig für REST-Controller mit Pfadvariablen.

In der `pom.xml` muss folgende Konfiguration für den Maven Compiler Plugin hinzugefügt werden:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <parameters>true</parameters>  <!-- Diese Zeile ist entscheidend! -->
    </configuration>
</plugin>
```

Alternativ kann man auch die Spring Boot-eigene Compiler-Konfiguration verwenden:

```xml
<properties>
    <java.version>17</java.version>
    <spring.boot.version>3.2.4</spring.boot.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <maven.compiler.parameters>true</maven.compiler.parameters>  <!-- Diese Zeile ist entscheidend! -->
</properties>
```

Ohne diese Konfiguration werden Fehler wie der folgende auftreten:
```
java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.
```

## Konfiguration und Deployment

### Umgebungsvariablen

Für die Konfiguration werden die folgenden Umgebungsvariablen verwendet:

- `PORT`: Server-Port (Standard: 8080)
- `OPENAI_API_KEY`: API-Schlüssel für OpenAI (erforderlich)
- `GOOGLE_VISION_API_KEY`: API-Schlüssel für Google Vision (optional)
- `ACTIVE_PRODUKTTYP_ID`: Aktiver Produkttyp (Standard: fashion)
- `API_LANGUAGE`: Standardsprache für API-Antworten (Standard: de)

### Docker-Deployment

Mit Docker Compose:

```bash
docker-compose up
```

Oder direkt mit Docker:

```bash
docker build -t pim-ai-connector-lite .
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your_openai_key \
  -e GOOGLE_VISION_API_KEY=your_vision_key \
  -e ACTIVE_PRODUKTTYP_ID=fashion \
  -e API_LANGUAGE=de \
  pim-ai-connector-lite
```

### Lokales Deployment

```bash
mvn clean package -Dmaven.compiler.parameters=true
java -jar target/pim-ai-connector-lite-0.0.1-SNAPSHOT.jar
```

## API-Referenz

### Produktbeschreibungen generieren

#### Nur Text

```
POST /api/enrich/gpt
```

Request-Body:
```json
{
  "p1": "Nike",
  "p2": "Laufschuhe",
  "p3": "Schwarz mit roten Akzenten",
  "p4": "Synthetik und Mesh",
  "p5": "EU 42"
}
```

#### Sprachspezifische Beschreibungen

```
POST /api/enrich/gpt/{language}
```
Wobei `{language}` entweder `de` oder `en` ist.

**Wichtig:** Bei der Verwendung von Pfadvariablen in Spring-Controllern muss der Parameter `@PathVariable(name = "language")` explizit benannt werden oder der Compiler muss mit dem `-parameters`-Flag kompiliert werden.

#### Mit Bildanalyse

```
POST /api/vision/analyzeAndText
```

Request-Body:
```json
{
  "p1": "Nike",
  "p2": "Laufschuhe",
  "p3": "Schwarz mit roten Akzenten",
  "p4": "Synthetik und Mesh",
  "p5": "EU 42",
  "imageBase64": "BASE64_ENCODED_IMAGE"
}
```

### Sprachkonfiguration

#### Aktuelle Sprache abrufen

```
GET /api/language/current
```

#### Sprache ändern

```
POST /api/language/set?language=en
```

#### Unterstützte Sprachen abrufen

```
GET /api/language/supported
```

### Produkttyp-Konfiguration

#### Aktive Konfiguration abrufen

```
GET /api/vision/config
```

#### Spezifischen Produkttyp abrufen

```
GET /api/vision/config?type=electronics
```

**Wichtig:** Bei der Verwendung von @RequestParam sollte der Parameter explizit benannt werden, z.B. `@RequestParam(name = "type", required = false) String produktTypId`, oder der Compiler muss mit dem `-parameters`-Flag kompiliert werden.

#### Alle Produkttypen abrufen

```
GET /api/vision/allProductTypes
```

#### Aktiven Produkttyp ändern

```
POST /api/vision/setProductType?type=electronics
```

## Fehlerbehebung

### Häufige Fehlermeldungen

#### Parameter-Name-Fehler

```
java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.
```

**Lösung:** Stellen Sie sicher, dass der Maven-Compiler mit dem `-parameters`-Flag konfiguriert ist, oder benennen Sie alle Parameter in den Controller-Methoden explizit, z.B. `@PathVariable(name = "language")` und `@RequestParam(name = "type")`.

#### API-Schlüssel-Fehler

```
API key not valid. Please pass a valid API key.
```

**Lösung:** Überprüfen Sie den Google Vision API-Schlüssel in den Umgebungsvariablen. Stellen Sie sicher, dass er mit `AIza` beginnt und nicht mit `Alza`.

#### Multipart-Request-Fehler

```
Current request is not a multipart request
```

**Lösung:** Stellen Sie sicher, dass der richtige Content-Type-Header (`multipart/form-data`) für Bildupload-Anfragen gesetzt ist.

## Web-Interface

Das Web-Interface ist unter der Basis-URL erreichbar (z.B. http://localhost:8080) und bietet:

- Demo zur Generierung von Produktbeschreibungen
- Auswahl verschiedener Produkttypen
- Sprachumschaltung (Deutsch/Englisch)
- Optionale Bildanalyse
- Beispiel-Anfragen und -Antworten

## Sprachunterstützung

Der PIM-AI-Connector-Lite bietet mehrsprachige Unterstützung für:

- **Deutsch (de)**: Standardsprache
- **Englisch (en)**: Vollständig unterstützt
- Weitere Sprachen können durch Erweiterung der `LanguageConfigService`-Klasse hinzugefügt werden

Die Sprachunterstützung umfasst:

- UI-Elemente und Beschriftungen
- Generierte Produktbeschreibungen
- Fehlermeldungen und Statustexte
- API-Antworten und Dokumentation

## Produkttypen

Die Anwendung unterstützt derzeit die folgenden Produkttypen:

1. **Bekleidung (fashion)**
   - Parameter: Marke, Kategorie, Farbe, Material, Größe, Muster, Stil, Anlass, Pflegehinweise, Herkunftsland

2. **Elektronik (electronics)**
   - Parameter: Marke, Produktart, Modell, Displaygröße, Speicher, Prozessor, Betriebssystem, Akkulaufzeit, Kamera, Konnektivität

3. **Möbel (furniture)**
   - Parameter: Marke, Möbeltyp, Farbe, Material, Stil, Abmessungen, Belastbarkeit, Pflege, Montage, Garantie

Neue Produkttypen können in der `pim-config.xml` Datei definiert werden.

## Erweiterbarkeit

Der PIM-AI-Connector-Lite wurde mit Erweiterbarkeit im Sinn entwickelt:

- **Neue Produkttypen**: Einfach durch Bearbeiten der XML-Konfiguration hinzufügbar
- **Zusätzliche Sprachen**: Durch Erweiterung der Sprachkonfiguration unterstützbar
- **API-Erweiterungen**: Modulare Controller-Struktur für einfache Endpunkt-Erweiterungen
- **KI-Modell-Anpassungen**: Konfigurierbare Prompt-Templates für verschiedene Anwendungsfälle

## Beiträge und Entwicklung

Um an diesem Projekt beizutragen:

1. Repository forken
2. Feature-Branch erstellen (`git checkout -b feature/amazing-feature`)
3. Änderungen committen (`git commit -m 'Add amazing feature'`)
4. Branch pushen (`git push origin feature/amazing-feature`)
5. Pull Request erstellen

## Lizenz

Dieses Projekt ist urheberrechtlich geschützt und steht unter einer privaten Lizenz.

## Kontakt

DNguyen - [n.thanh@gmx.de](mailto:n.thanh@gmx.de)

Projektlink: [https://github.com/username/pim-ai-connector-lite](https://github.com/username/pim-ai-connector-lite)