# API-Key-Authentifizierung für PIM-AI-Connector-Lite

Diese Dokumentation beschreibt die Implementation der API-Key-Authentifizierung für den PIM-AI-Connector-Lite, um die API-Endpunkte vor unberechtigtem Zugriff zu schützen.

## Übersicht

Die Authentifizierung erfolgt über einen API-Schlüssel, der bei jeder Anfrage an die geschützten Endpunkte übergeben werden muss. Die Implementierung unterstützt folgende Funktionen:

- Schutz aller API-Endpunkte unter `/api/`
- Konfigurierbare Ausnahmen für bestimmte Endpunkte
- Freier Zugriff auf statische Ressourcen und die Web-Oberfläche
- Möglichkeit, die API-Key-Authentifizierung komplett zu deaktivieren
- Internationalisierte Fehlermeldungen (DE/EN)

## Konfiguration

Die API-Key-Authentifizierung wird über folgende Umgebungsvariablen konfiguriert:

- `API_KEY`: Der zu verwendende API-Schlüssel (Standard: "demo-key")
- `ENABLE_API_KEY`: Aktiviert/deaktiviert die API-Key-Authentifizierung (Standard: true)

### Konfiguration in .env-Datei

```
API_KEY=ihr-geheimer-api-schlüssel
ENABLE_API_KEY=true
```

### Konfiguration in Docker Compose

```yaml
services:
  pes:
    image: pim-ai-connector-lite
    environment:
      - API_KEY=ihr-geheimer-api-schlüssel
      - ENABLE_API_KEY=true
```

## Verwendung des API-Keys

Der API-Schlüssel kann auf zwei Arten an die API übergeben werden:

### 1. Als HTTP-Header

```
X-API-Key: ihr-geheimer-api-schlüssel
```

### 2. Als URL-Parameter

```
https://your-service.com/api/enrich/gpt?apiKey=ihr-geheimer-api-schlüssel
```

## Beispiele

### cURL-Anfrage mit API-Key als Header

```bash
curl -X POST http://localhost:8080/api/enrich/gpt \
  -H "Content-Type: application/json" \
  -H "X-API-Key: ihr-geheimer-api-schlüssel" \
  -d '{
    "p1": "Nike",
    "p2": "Laufschuhe",
    "p3": "Schwarz mit roten Akzenten",
    "p4": "Synthetik und Mesh",
    "p5": "EU 42"
  }'
```

### JavaScript-Fetch mit API-Key als Header

```javascript
// Beispiel für die Integration in die Web-Oberfläche
fetch('/api/enrich/gpt', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-API-Key': 'ihr-geheimer-api-schlüssel'
  },
  body: JSON.stringify({
    p1: 'Nike',
    p2: 'Laufschuhe',
    p3: 'Schwarz mit roten Akzenten',
    p4: 'Synthetik und Mesh',
    p5: 'EU 42'
  })
})
.then(response => response.text())
.then(data => console.log(data))
.catch(error => console.error('Fehler:', error));
```

## Sicherheitshinweise

- Ändern Sie den Standard-API-Schlüssel (`demo-key`) vor der Produktionsnutzung
- Verwenden Sie einen sicheren, zufällig generierten API-Schlüssel (z.B. mit mindestens 32 Zeichen)
- Der API-Schlüssel sollte regelmäßig rotiert werden
- Für produktive Umgebungen sollte zusätzlich eine HTTPS-Verbindung verwendet werden
- In hochsicheren Umgebungen sollte diese einfache API-Key-Authentifizierung durch eine fortgeschrittenere Lösung wie OAuth2 oder JWT ersetzt werden

## Fehlerbehebung

Bei einem ungültigen API-Schlüssel wird ein HTTP 401 (Unauthorized) mit folgender JSON-Antwort zurückgegeben:

```json
{
  "error": "Ungültiger API-Key",
  "status": 401
}
```

Für englische Endpunkte:

```json
{
  "error": "Invalid API key",
  "status": 401
}
```

## Anpassung der Web-Oberfläche

Die Web-Oberfläche muss angepasst werden, um den API-Schlüssel bei allen API-Anfragen zu übermitteln. Hierzu sollten alle Fetch-Aufrufe um den entsprechenden Header ergänzt werden.