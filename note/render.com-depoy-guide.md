# Render.com Deployment Guide für PIM-AI-Connector

## Schritt 1: Render.com Account erstellen
- Registriere dich auf https://render.com
- Bestätige deine E-Mail-Adresse

## Schritt 2: Neuen Web Service erstellen
1. Klicke auf "New +" und wähle "Web Service"
2. Verbinde dein GitHub-Repository oder lade das Projekt manuell hoch
3. Gib deinem Service einen Namen: "pim-ai-connector-lite"

## Schritt 3: Service konfigurieren
- **Runtime:** Java
- **Build Command:** `./mvnw clean package -DskipTests`
- **Start Command:** `java -Dserver.port=$PORT -jar target/pim-ai-connector-lite.jar`
- **Instance Type:** Free (für Tests) oder Basic (für Produktion)

## Schritt 4: Umgebungsvariablen einrichten
Füge folgende Umgebungsvariablen hinzu:
```
WEB_DOMAIN=pim-ai-connector-lite.onrender.com
API_LANGUAGE=en
API_KEY=demo-key
ENABLE_API_KEY=true
OPENAI_API_KEY=your_openai_api_key
GOOGLE_VISION_API_KEY=your_google_vision_api_key
ACTIVE_PRODUKTTYP_ID=electronics
```

## Schritt 5: Advanced Settings
- **Health Check Path:** `/api/health`
- **Auto Deploy:** Enabled (Optional)

## Schritt 6: Deployment starten
- Klicke auf "Create Web Service"
- Render wird den Build-Prozess starten und den Service deployen

## Schritt 7: Domain konfigurieren
- Nach erfolgreicher Bereitstellung erhältst du eine URL wie: `https://pim-ai-connector-lite.onrender.com`
- Du kannst auch eine benutzerdefinierte Domain einrichten (unter Einstellungen → Custom Domain)

## Schritt 8: Testing
- Verwende die test-api.http und test-vision.http Dateien, um deinen Service zu testen
- Aktualisiere die @apiBaseUrl auf deine Render.com Domain:
  ```
  @apiBaseUrl = https://pim-ai-connector-lite.onrender.com
  ```

## Fehlerbehandlung

### Problem: Build schlägt fehl
- Überprüfe deine pom.xml auf Fehler
- Stelle sicher, dass der Maven Wrapper (.mvnw) im Repository enthalten ist
- Logs auf Render.com für weitere Details prüfen

### Problem: Service startet nicht
- Überprüfe die Logs auf Render.com
- Stelle sicher, dass alle erforderlichen Umgebungsvariablen korrekt eingerichtet sind
- Überprüfe, ob die JAR-Datei korrekt erstellt wurde

### Problem: API-Anfragen schlagen fehl
- Überprüfe, ob die API-Key-Authentifizierung korrekt eingerichtet ist
- Stelle sicher, dass CORS für deine Domain konfiguriert ist
- Überprüfe die OpenAI und Google Vision API-Keys

### Problem: CORS-Fehler
- Stelle sicher, dass die WebConfig-Klasse korrekt für Render.com konfiguriert ist
- Überprüfe, ob WEB_DOMAIN korrekt gesetzt ist

## Nützliche Render.com Funktionen
- **Logs:** Echtzeit-Logs für Debugging
- **Metrics:** Überwache die Leistung deines Services
- **Auto-Deploy:** Automatische Bereitstellung bei GitHub-Commits
- **Preview Environments:** Teste Änderungen, bevor sie in die Produktion gehen