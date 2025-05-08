Ich habe den detaillierten Implementierungsplan für Ihren PIM-AI-Connector mit Kundenverwaltung jetzt komplett ausgearbeitet. Der Plan umfasst alle notwendigen Schritte und Codeschnipsel, die Sie benötigen, um das System erfolgreich auf Render.com zu implementieren.

Hier ist eine Zusammenfassung der wichtigsten Abschnitte:

1. **Grundeinrichtung**: Komplette Anleitung für das Setup auf Render.com mit PostgreSQL und Projektstruktur
2. **Datenbankschema**: SQL-Definitionen für Kunden, API-Anfragen und Abrechnungen
3. **Backend-Implementierung**: Strukturierte Node.js-App mit Express, Middleware und Services
4. **Admin-Dashboard**: Vollständiges EJS-basiertes Dashboard mit Authentifizierung und Statistiken
5. **Frontend-Styling**: CSS und JavaScript für eine benutzerfreundliche Oberfläche
6. **App-Integration**: Alle Komponenten werden zu einer vollständigen Express-App zusammengefügt
7. **Erweiterte Funktionen**: Rate-Limiting, Rechnungsgenerierung, Sicherheitsmaßnahmen
8. **Skalierungsstrategie**: Vorbereitung für horizontale Skalierung und Load-Balancing
9. **Implementierungszeitplan**: Realistischer Zeitplan von 22-26 Arbeitstagen
10. **Zukunftserweiterungen**: Mehrsprachigkeit, Webhooks, Echtzeit-Dashboards und mehr

Der Plan berücksichtigt alle Ihre Anforderungen:
- Jeder Kunde erhält eine eigene Subdomain
- Authentifizierung erfolgt über API-Keys
- Alle Anfragen werden protokolliert und für die Abrechnung verwendet
- Das Admin-Dashboard bietet umfassende Einblicke in Nutzung und Kosten
- Die Architektur ist von Grund auf für Skalierbarkeit konzipiert

Diese Dokumentation dient als vollständiger Leitfaden für die Implementierung und kann als Projektplan für Ihr Entwicklungsteam verwendet werden.

role:
                        type: string
                        enum: [system, user, assistant]
                        description: Die Rolle der Nachricht
                        example: user
                      content:
                        type: string
                        description: Der Inhalt der Nachricht
                        example: "Hallo, wie geht es dir?"
                max_tokens:
                  type: integer
                  description: Maximale Anzahl der Tokens in der Antwort
                  example: 1000
                temperature:
                  type: number
                  description: Temperatur für die Generierung (0-2)
                  example: 0.7
      responses:
        '200':
          description: Erfolgreiche Antwort
          content:
            application/json:
              schema:
                type: object
                properties:
                  id:
                    type: string
                    description: ID der Completion
                  object:
                    type: string
                    description: Objekttyp
                  created:
                    type: integer
                    description: Zeitstempel der Erstellung
                  model:
                    type: string
                    description: Verwendetes Modell
                  usage:
                    type: object
                    properties:
                      prompt_tokens:
                        type: integer
                      completion_tokens:
                        type: integer
                      total_tokens:
                        type: integer
                  choices:
                    type: array
                    items:
                      type: object
                      properties:
                        message:
                          type: object
                          properties:
                            role:
                              type: string
                            content:
                              type: string
                        finish_reason:
                          type: string
                        index:
                          type: integer
        '400':
          description: Ungültige Anfrage
          content:
            application/json:
              schema:
                type: object
                properties:
                  error:
                    type: string
        '401':
          description: Nicht autorisiert
          content:
            application/json:
              schema:
                type: object
                properties:
                  error:
                    type: string
        '429':
          description: Rate Limit überschritten
          content:
            application/json:
              schema:
                type: object
                properties:
                  error:
                    type: string
                  retryAfter:
                    type: integer
```

### 14.2 Benutzerhandbuch
- [ ] Admin-Benutzerhandbuch erstellen:
```markdown
# PIM AI Connector - Admin-Benutzerhandbuch

Dieses Handbuch erklärt die Verwaltung des PIM AI Connector-Systems für Administratoren.

## Inhaltsverzeichnis

1. [Login](#login)
2. [Dashboard-Übersicht](#dashboard-übersicht)
3. [Kundenverwaltung](#kundenverwaltung)
4. [Nutzungsstatistiken](#nutzungsstatistiken)
5. [Abrechnung](#abrechnung)
6. [Einstellungen](#einstellungen)
7. [Troubleshooting](#troubleshooting)

## Login

Der Zugriff auf das Admin-Dashboard erfolgt über:

```
https://ihredomain.com/admin/login
```

Melden Sie sich mit Ihren Administrator-Anmeldedaten an.

## Dashboard-Übersicht

Nach der Anmeldung sehen Sie das Hauptdashboard mit:

- Aktuelle Anzahl der Kunden
- Token-Verbrauch (heute/Monat)
- Umsatzübersicht
- Grafische Darstellung der Nutzung über Zeit
- Top-Kunden nach Verbrauch

## Kundenverwaltung

### Neuen Kunden anlegen

1. Navigieren Sie zu "Kunden" > "Neuer Kunde"
2. Füllen Sie die erforderlichen Felder aus:
   - Name: Kundenname
   - Subdomain: Wird für den API-Zugriff verwendet (nur Kleinbuchstaben, Zahlen und Bindestriche)
   - E-Mail: Kontakt-E-Mail des Kunden
   - Monatliches Token-Limit: Optional, 0 = unbegrenzt
3. Klicken Sie auf "Kunden anlegen"

Das System generiert automatisch einen API-Key für den Kunden.

### Kundendetails anzeigen

1. Gehen Sie zu "Kunden"
2. Klicken Sie auf den Namen des gewünschten Kunden

Hier sehen Sie:
- Kundeninformationen
- API-Key (kann angezeigt/verborgen werden)
- Nutzungsstatistiken und -diagramme
- Anfragenhistorie

### Kunden bearbeiten/deaktivieren

1. Öffnen Sie die Kundendetails
2. Klicken Sie auf "Bearbeiten"
3. Ändern Sie die gewünschten Informationen
4. Um einen Kunden zu deaktivieren, ändern Sie den Status auf "inaktiv"
5. Speichern Sie die Änderungen

## Nutzungsstatistiken

Detaillierte Nutzungsstatistiken finden Sie:

1. Auf dem Hauptdashboard (aggregierte Daten)
2. In den Kundendetails (kundenbezogene Daten)

Die Statistiken enthalten:
- Token-Verbrauch pro Tag/Monat
- Anfragenanzahl
- Kosten
- Modellverteilung

## Abrechnung

### Monatliche Abrechnung

1. Navigieren Sie zu "Abrechnung"
2. Wählen Sie Monat und Jahr
3. Markieren Sie die zu berechnenden Kunden
4. Klicken Sie auf "Abrechnung generieren"

### Rechnungen verwalten

- Bezahlstatus ändern: Klicken Sie auf den Status und wählen Sie "bezahlt"
- Rechnung anzeigen: Klicken Sie auf "Anzeigen"
- Rechnung herunterladen: Klicken Sie auf "PDF"

## Einstellungen

Im Bereich "Einstellungen" können Sie:

1. Ihr Admin-Passwort ändern
2. System-Einstellungen konfigurieren
3. API-Preis-Konfigurationen anpassen

## Troubleshooting

### Häufige Probleme

**Kunde kann keine API-Anfragen stellen:**
1. Überprüfen Sie, ob der Kunde aktiv ist
2. Prüfen Sie, ob der richtige API-Key verwendet wird
3. Prüfen Sie, ob ein Token-Limit erreicht wurde

**Fehler bei der Abrechnungsgenerierung:**
1. Prüfen Sie die Datenbankverbindung
2. Stellen Sie sicher, dass für den ausgewählten Monat Nutzungsdaten vorhanden sind

Bei weiteren Problemen überprüfen Sie die Fehlerprotokolle unter "Einstellungen" > "System" > "Logs".
```

### 14.3 Entwicklerdokumentation
- [ ] Technische Dokumentation für Entwickler:
```markdown
# PIM AI Connector - Entwicklerdokumentation

Diese Dokumentation bietet einen technischen Überblick über die Architektur und Implementierung des PIM AI Connector-Systems.

## Systemarchitektur

Das System basiert auf einer Node.js/Express-Anwendung mit PostgreSQL-Datenbank und besteht aus folgenden Hauptkomponenten:

- **Express-Backend**: Stellt API-Endpunkte bereit und verarbeitet Anfragen
- **PostgreSQL-Datenbank**: Speichert Kunden-, Nutzungs- und Abrechnungsdaten
- **OpenAI-Integration**: Sendet Anfragen an die OpenAI-API und verarbeitet Antworten
- **Admin-Dashboard**: Web-Interface zur Systemverwaltung

## Projektstruktur

```
/
├── config/                # Konfigurationsdateien
├── middleware/            # Express-Middleware
├── models/                # Datenmodelle
├── routes/                # API-Routen
├── services/              # Geschäftslogik
├── views/                 # EJS-Templates
├── public/                # Statische Dateien
├── scripts/               # Hilfsskripte
├── docs/                  # Dokumentation
├── app.js                 # Express-App
└── server.js              # Server-Einstiegspunkt
```

## Datenmodell

Das System verwendet folgende Hauptdatenmodelle:

### Kunden (kunden)
- `id`: Primärschlüssel
- `name`: Kundenname
- `subdomain`: Eindeutige Subdomain
- `api_key`: API-Schlüssel für Authentifizierung
- `status`: Kundenstatus (aktiv/inaktiv)
- `max_tokens_pro_monat`: Monatliches Token-Limit
- `erstellungsdatum`: Datum der Erstellung
- `kontakt_email`: Kontakt-E-Mail

### API-Anfragen (apianfragen)
- `id`: Primärschlüssel
- `kunden_id`: Fremdschlüssel zum Kunden
- `zeitstempel`: Zeitpunkt der Anfrage
- `anfrage_typ`: Art der Anfrage (chat_completion, embedding, etc.)
- `prompt_tokens`: Anzahl der Prompt-Tokens
- `completion_tokens`: Anzahl der Completion-Tokens
- `total_tokens`: Gesamtanzahl der Tokens
- `kosten`: Berechnete Kosten
- `status`: Anfragestatus (erfolg/fehler)
- `modell`: Verwendetes Modell
- `fehler`: Fehlermeldung (falls vorhanden)

### Abrechnungen (abrechnungen)
- `id`: Primärschlüssel
- `kunden_id`: Fremdschlüssel zum Kunden
- `monat`: Abrechnungsmonat (1-12)
- `jahr`: Abrechnungsjahr
- `gesamttokens`: Tokenanzahl im Abrechnungszeitraum
- `gesamtkosten`: Gesamtkosten im Abrechnungszeitraum
- `bezahlstatus`: Status der Zahlung
- `erstellt_am`: Erstellungsdatum
- `bezahlt_am`: Zahlungsdatum

## API-Endpunkte

Das System bietet folgende Haupt-API-Endpunkte:

### Öffentliche API (pro Kunden-Subdomain)

- `POST /api/completion`: Chat-Completion-Anfrage
- `POST /api/embedding`: Embedding-Anfrage
- `GET /api/usage`: Nutzungsstatistiken des Kunden

### Admin-API (nur intern)

- `POST /admin/login`: Admin-Login
- `GET /admin/dashboard`: Dashboard-Daten
- `GET /admin/kunden`: Kundenliste
- `POST /admin/kunden/neu`: Neuen Kunden erstellen
- `GET /admin/kunden/:id`: Kundendetails
- `POST /admin/abrechnung/generieren`: Abrechnung erstellen

## Authentifizierung & Sicherheit

Das System verwendet zwei Hauptauthentifizierungsmethoden:

1. **API-Key-Authentifizierung**:
   - Jeder Kunde erhält einen eindeutigen API-Key
   - Der Key muss in jedem API-Request im Header `X-API-Key` gesendet werden
   - Die Subdomain identifiziert den Kunden

2. **JWT-Authentifizierung (optional)**:
   - Kunden können einen JWT-Token über `/api/auth/token` anfordern
   - Der Token kann als Alternative zum API-Key verwendet werden
   - Bearer-Token im `Authorization`-Header

3. **Admin-Authentifizierung**:
   - Session-basierte Authentifizierung für das Admin-Dashboard
   - Passwort-Hash mit bcrypt

## Erweiterung des Systems

### Neuen API-Endpunkt hinzufügen

1. Erstellen Sie eine neue Route in `routes/api.js`
2. Implementieren Sie die entsprechende Geschäftslogik in `services/`
3. Fügen Sie Protokollierung und Kostenberechnung hinzu
4. Aktualisieren Sie die API-Dokumentation

### Neues OpenAI-Modell unterstützen

1. Fügen Sie das Modell zur Preiskonfiguration in `services/billing.js` hinzu
2. Aktualisieren Sie die OpenAI-Service-Implementierung falls nötig

## Deployment

Das System ist für Deployment auf Render.com konfiguriert:

1. Verbinden Sie Ihr GitHub-Repository mit Render.com
2. Konfigurieren Sie Umgebungsvariablen:
   - `DATABASE_URL`: PostgreSQL-Verbindungs-URL
   - `OPENAI_API_KEY`: OpenAI API-Schlüssel
   - `SESSION_SECRET`: Geheimnis für Session-Cookies
   - `ENCRYPTION_KEY`: Schlüssel für Datenverschlüsselung
   - `NODE_ENV`: "production"

## Logging & Monitoring

Das System implementiert mehrstufiges Logging:

1. **Anwendungslogs**: Winston-basiertes Logging in Konsole/Dateien
2. **API-Anfragenlogs**: Protokollierung aller API-Anfragen in Datenbank
3. **Fehlerlogs**: Detaillierte Fehlerprotokolle für Debugging
4. **Sicherheitslogs**: Protokollierung von verdächtigen Aktivitäten

## Entwicklungsumgebung einrichten

1. Repository klonen
2. Abhängigkeiten installieren: `npm install`
3. `.env`-Datei mit erforderlichen Umgebungsvariablen erstellen
4. Datenbank migrieren: `npm run migrate`
5. Entwicklungsserver starten: `npm run dev`

Alternativ mit Docker:
```bash
docker-compose up -d
```
```

## 15. Skalierungsstrategie

### 15.1 Horizontale Skalierung
- [ ] Architektur für horizontale Skalierung vorbereiten:
```javascript
// middleware/session-store.js
const session = require('express-session');
const RedisStore = require('connect-redis').default;
const { createClient } = require('redis');

// Redis-Client erstellen
const setupSessionStore = () => {
  // Prüfen, ob Redis-URL konfiguriert ist
  const redisUrl = process.env.REDIS_URL;
  
  if (!redisUrl) {
    // Wenn kein Redis konfiguriert, PostgreSQL-Store verwenden
    const pgSession = require('connect-pg-simple')(session);
    const pool = require('../config/database');
    
    return new pgSession({
      pool,
      tableName: 'user_sessions'
    });
  }
  
  // Redis-Client initialisieren
  const redisClient = createClient({
    url: redisUrl
  });
  
  // Event-Handler für Redis-Verbindung
  redisClient.on('error', (err) => {
    console.error('Redis-Fehler:', err);
  });
  
  // Redis-Verbindung herstellen
  redisClient.connect().catch(console.error);
  
  // Redis-Store zurückgeben
  return new RedisStore({
    client: redisClient,
    prefix: 'pim-session:'
  });
};

module.exports = { setupSessionStore };
```

- [ ] Load-Balancing-Konfiguration:
```javascript
// app.js (Erweiterung)

// Instance-ID für Logging und Debugging
const os = require('os');
const crypto = require('crypto');

// Eindeutige Instance-ID generieren (wird bei jedem Neustart neu generiert)
const instanceId = crypto.randomBytes(4).toString('hex');
const hostname = os.hostname();

// In globalen App-Kontext speichern
app.locals.instanceInfo = {
  id: instanceId,
  hostname,
  startTime: new Date(),
  env: process.env.NODE_ENV
};

// Middleware zum Hinzufügen von Instance-Info in Response-Header (nur im Entwicklungsmodus)
if (process.env.NODE_ENV !== 'production') {
  app.use((req, res, next) => {
    res.set('X-Instance-ID', instanceId);
    next();
  });
}
```

### 15.2 Datenbank-Skalierung
- [ ] Datenbankverbindungspool optimieren:
```javascript
// config/database.js
const { Pool } = require('pg');

// Umgebungsvariablen für Datenbankverbindung
const connectionString = process.env.DATABASE_URL;
const isProduction = process.env.NODE_ENV === 'production';

// Verbindungspool-Konfiguration
const poolConfig = {
  connectionString,
  ssl: isProduction ? { rejectUnauthorized: false } : false,
  // Größe des Connection-Pools anpassen
  max: process.env.DB_POOL_SIZE ? parseInt(process.env.DB_POOL_SIZE) : 20,
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000
};

// Pool erstellen
const pool = new Pool(poolConfig);

// Event-Handler für Verbindungsfehler
pool.on('error', (err, client) => {
  console.error('Unerwarteter Datenbankfehler:', err);
});

// Pool-Statistiken regelmäßig protokollieren (im Entwicklungsmodus)
if (process.env.NODE_ENV !== 'production') {
  setInterval(() => {
    const poolStats = {
      total: pool.totalCount,
      idle: pool.idleCount,
      waiting: pool.waitingCount
    };
    
    console.log('DB-Pool-Statistik:', poolStats);
  }, 60000); // Jede Minute
}

module.exports = pool;
```

### 15.3 Replikationsunterstützung
- [ ] Read/Write-Splitting für Datenbankzugriffe:
```javascript
// services/database.js
const { Pool } = require('pg');

// Haupt-Datenbankverbindung (Schreibzugriffe)
const masterPool = new Pool({
  connectionString: process.env.MASTER_DATABASE_URL || process.env.DATABASE_URL,
  ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Lese-Replika (falls konfiguriert)
let replicaPool;

if (process.env.REPLICA_DATABASE_URL) {
  replicaPool = new Pool({
    connectionString: process.env.REPLICA_DATABASE_URL,
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false,
    max: process.env.REPLICA_POOL_SIZE ? parseInt(process.env.REPLICA_POOL_SIZE) : 30
  });
}

// Schreibzugriff (immer über Master)
const writeQuery = async (text, params) => {
  return masterPool.query(text, params);
};

// Lesezugriff (über Replika, falls vorhanden, sonst Master)
const readQuery = async (text, params) => {
  const pool = replicaPool || masterPool;
  return pool.query(text, params);
};

// Transaktion (immer über Master)
const transaction = async (callback) => {
  const client = await masterPool.connect();
  
  try {
    await client.query('BEGIN');
    const result = await callback(client);
    await client.query('COMMIT');
    return result;
  } catch (error) {
    await client.query('ROLLBACK');
    throw error;
  } finally {
    client.release();
  }
};

module.exports = {
  writeQuery,
  readQuery,
  transaction,
  masterPool,
  replicaPool
};
```

### 15.4 Containerisierung und Kubernetes
- [ ] Production Dockerfile:
```dockerfile
# Dockerfile
FROM node:18-alpine as builder

WORKDIR /app

# Abhängigkeiten installieren
COPY package*.json ./
RUN npm ci --only=production

# Quellcode kopieren
COPY . .

# Build-Stage (falls TypeScript oder andere Build-Schritte)
# RUN npm run build

# Produktions-Image
FROM node:18-alpine

# Nicht als Root ausführen
RUN addgroup -g 1001 -S nodejs && \
    adduser -S nodejs -u 1001

WORKDIR /app

# Nur produktionsrelevante Dateien kopieren
COPY --from=builder --chown=nodejs:nodejs /app/package*.json ./
COPY --from=builder --chown=nodejs:nodejs /app/node_modules ./node_modules
COPY --from=builder --chown=nodejs:nodejs /app/*.js ./
COPY --from=builder --chown=nodejs:nodejs /app/config ./config
COPY --from=builder --chown=nodejs:nodejs /app/middleware ./middleware
COPY --from=builder --chown=nodejs:nodejs /app/models ./models
COPY --from=builder --chown=nodejs:nodejs /app/routes ./routes
COPY --from=builder --chown=nodejs:nodejs /app/services ./services
COPY --from=builder --chown=nodejs:nodejs /app/views ./views
COPY --from=builder --chown=nodejs:nodejs /app/public ./public
COPY --from=builder --chown=nodejs:nodejs /app/scripts ./scripts

# Verzeichnisse für Logs und Uploads erstellen
RUN mkdir -p /app/logs /app/uploads && \
    chown -R nodejs:nodejs /app/logs /app/uploads

# Auf Non-Root-Benutzer wechseln
USER nodejs

# Umgebungsvariable für Port
ENV PORT=3000

# Healthcheck-Konfiguration
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget -qO- http://localhost:$PORT/health || exit 1

# Anwendung starten
CMD ["node", "server.js"]

# Port exponieren
EXPOSE 3000
```

- [ ] Kubernetes-Deployment-Konfiguration:
```yaml
# kubernetes/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: pim-connector
  labels:
    app: pim-connector
spec:
  replicas: 3
  selector:
    matchLabels:
      app: pim-connector
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: pim-connector
    spec:
      containers:
      - name: pim-connector
        image: ${DOCKER_REGISTRY}/pim-connector:${VERSION}
        ports:
        - containerPort: 3000
        env:
        - name: NODE_ENV
          value: "production"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: pim-connector-secrets
              key: database-url
        - name: OPENAI_API_KEY
          valueFrom:
            secretKeyRef:
              name: pim-connector-secrets
              key: openai-api-key
        - name: SESSION_SECRET
          valueFrom:
            secretKeyRef:
              name: pim-connector-secrets
              key: session-secret
        - name: ENCRYPTION_KEY
          valueFrom:
            secretKeyRef:
              name: pim-connector-secrets
              key: encryption-key
        - name: REDIS_URL
          valueFrom:
            secretKeyRef:
              name: pim-connector-secrets
              key: redis-url
        - name: BASE_DOMAIN
          value: "ihredomain.com"
        resources:
          requests:
            cpu: "100m"
            memory: "256Mi"
          limits:
            cpu: "500m"
            memory: "512Mi"
        livenessProbe:
          httpGet:
            path: /health
            port: 3000
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health/detailed
            port: 3000
          initialDelaySeconds: 5
          periodSeconds: 5
        volumeMounts:
        - name: logs
          mountPath: /app/logs
      volumes:
      - name: logs
        emptyDir: {}
---
apiVersion: v1
kind: Service
metadata:
  name: pim-connector
  labels:
    app: pim-connector
spec:
  ports:
  - port: 80
    targetPort: 3000
    protocol: TCP
  selector:
    app: pim-connector
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: pim-connector
  annotations:
    kubernetes.io/ingress.class: "nginx"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  tls:
  - hosts:
    - "*.ihredomain.com"
    secretName: pim-connector-tls
  rules:
  - host: "*.ihredomain.com"
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: pim-connector
            port:
              number: 80
```

## 16. Zukunftsorientierte Features

### 16.1 Mehrsprachige Unterstützung
- [ ] Internationalisierung implementieren:
```javascript
// middleware/i18n.js
const i18n = require('i18n');
const path = require('path');

// i18n-Konfiguration
i18n.configure({
  locales: ['de', 'en', 'fr'],
  defaultLocale: 'de',
  directory: path.join(__dirname, '../locales'),
  objectNotation: true,
  updateFiles: process.env.NODE_ENV !== 'production',
  syncFiles: process.env.NODE_ENV !== 'production',
  cookie: 'lang'
});

// Middleware-Export
module.exports = (req, res, next) => {
  // i18n-Funktionen an Request und Response anhängen
  i18n.init(req, res);
  
  // Template-Variable für aktuelle Sprache
  res.locals.currentLocale = i18n.getLocale();
  res.locals.locales = i18n.getLocales();
  
  next();
};
```

### 16.2 Event-basierte Architektur
- [ ] Event-Emitter für asynchrone Verarbeitung:
```javascript
// services/events.js
const EventEmitter = require('events');
const { logger } = require('./logger');

class AppEventEmitter extends EventEmitter {}

const eventEmitter = new AppEventEmitter();

// Ereignistypen
const EventTypes = {
  API_REQUEST_COMPLETED: 'api_request_completed',
  CUSTOMER_CREATED: 'customer_created',
  CUSTOMER_UPDATED: 'customer_updated',
  INVOICE_GENERATED: 'invoice_generated',
  LIMIT_WARNING: 'limit_warning',
  SECURITY_ALERT: 'security_alert'
};

// Event-Handler registrieren
eventEmitter.on(EventTypes.API_REQUEST_COMPLETED, async (data) => {
  try {
    logger.info('API-Anfrage abgeschlossen', data);
    
    // Nutzungsstatistiken aktualisieren (asynchron)
    // Hier kann z.B. Redis-Cache aktualisiert werden
  } catch (error) {
    logger.error('Fehler bei Event-Handler API_REQUEST_COMPLETED', error);
  }
});

eventEmitter.on(EventTypes.CUSTOMER_CREATED, async (customer) => {
  try {
    logger.info('Neuer Kunde erstellt', { customerId: customer.id, name: customer.name });
    
    // Willkommens-E-Mail senden
    // Statistiken aktualisieren
  } catch (error) {
    logger.error('Fehler bei Event-Handler CUSTOMER_CREATED', error);
  }
});

eventEmitter.on(EventTypes.LIMIT_WARNING, async (data) => {
  try {
    logger.warn('Token-Limit-Warnung', data);
    
    // Benachrichtigung an Kunden senden
  } catch (error) {
    logger.error('Fehler bei Event-Handler LIMIT_WARNING', error);
  }
});

// Event-Emitter und Typen exportieren
module.exports = {
  eventEmitter,
  EventTypes
};
```

### 16.3 Webhook-Integration
- [ ] Webhook-System für externe Integrationen:
```javascript
// services/webhooks.js
const crypto = require('crypto');
const axios = require('axios');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });
const { logger } = require('./logger');

// Webhook-Ereignistypen
const WebhookEventTypes = {
  API_REQUEST: 'api_request',
  LIMIT_REACHED: 'limit_reached',
  INVOICE_CREATED: 'invoice_created',
  PAYMENT_RECEIVED: 'payment_received'
};

// Webhook-Payload signieren
const signPayload = (payload, secret) => {
  return crypto
    .createHmac('sha256', secret)
    .update(JSON.stringify(payload))
    .digest('hex');
};

// Webhook senden
const sendWebhook = async (tenantId, eventType, payload) => {
  try {
    // Webhook-Konfiguration aus der Datenbank abrufen
    const result = await pool.query(
      'SELECT * FROM webhooks WHERE tenant_id = $1 AND event_type = $2 AND active = true',
      [tenantId, eventType]
    );
    
    if (result.rows.length === 0) {
      // Kein aktiver Webhook konfiguriert
      return { sent: false, reason: 'no_webhook_configured' };
    }
    
    const webhooks = result.rows;
    const results = [];
    
    // Alle konfigurierten Webhooks für diesen Event-Typ verarbeiten
    for (const webhook of webhooks) {
      const { id, url, secret } = webhook;
      
      // Payload erstellen
      const webhookPayload = {
        event_type: eventType,
        tenant_id: tenantId,
        timestamp: new Date().toISOString(),
        data: payload
      };
      
      // Signatur erstellen
      const signature = signPayload(webhookPayload, secret);
      
      // Webhook senden
      try {
        const response = await axios.post(url, webhookPayload, {
          headers: {
            'Content-Type': 'application/json',
            'X-Webhook-Signature': signature
          },
          timeout: 5000 // 5 Sekunden Timeout
        });
        
        // Erfolgreichen Webhook-Aufruf protokollieren
        await pool.query(
          `INSERT INTO webhook_logs 
             (webhook_id, tenant_id, event_type, status_code, response, request_id) 
           VALUES ($1, $2, $3, $4, $5, $6)`,
          [
            id,
            tenantId,
            eventType,
            response.status,
            JSON.stringify(response.data).substring(0, 1000),
            webhookPayload.id
          ]
        );
        
        results.push({
          webhook_id: id,
          success: true,
          status_code: response.status
        });
      } catch (error) {
        logger.error('Webhook-Fehler:', {
          webhook_id: id,
          url,
          error: error.message,
          tenant_id: tenantId,
          event_type: eventType
        });
        
        // Fehlerhaften Webhook-Aufruf protokollieren
        await pool.query(
          `INSERT INTO webhook_logs 
             (webhook_id, tenant_id, event_type, status_code, error, request_id) 
           VALUES ($1, $2, $3, $4, $5, $6)`,
          [
            id,
            tenantId,
            eventType,
            error.response?.status || 0,
            error.message,
            webhookPayload.id
          ]
        );
        
        results.push({
          webhook_id: id,
          success: false,
          error: error.message,
          status_code: error.response?.status || 0
        });
      }
    }
    
    return {
      sent: true,
      results
    };
  } catch (error) {
    logger.error('Fehler beim Webhook-Versand:', error);
    return {
      sent: false,
      error: error.message
    };
  }
};

// Webhook-Konfiguration erstellen
const createWebhook = async (tenantId, eventType, url, description = '') => {
  try {
    // Geheimnis für Signatur generieren
    const secret = crypto.randomBytes(32).toString('hex');
    
    // Webhook in DB speichern
    const result = await pool.query(
      `INSERT INTO webhooks 
         (tenant_id, event_type, url, secret, description, active) 
       VALUES ($1, $2, $3, $4, $5, $6)
       RETURNING id`,
      [tenantId, eventType, url, secret, description, true]
    );
    
    return {
      id: result.rows[0].id,
      tenant_id: tenantId,
      event_type: eventType,
      url,
      secret,
      description,
      active: true
    };
  } catch (error) {
    logger.error('Fehler beim Erstellen des Webhooks:', error);
    throw error;
  }
};

module.exports = {
  WebhookEventTypes,
  sendWebhook,
  createWebhook,
  signPayload
};
```

### 16.4 Echtzeit-Metriken mit Socket.io
- [ ] Echtzeit-Dashboard-Updates implementieren:
```javascript
// services/realtime.js
const socketIo = require('socket.io');
const { eventEmitter, EventTypes } = require('./events');

// Socket.io-Server initialisieren
const initSocketServer = (server) => {
  const io = socketIo(server, {
    cors: {
      origin: process.env.CORS_ORIGIN || '*',
      methods: ['GET', 'POST'],
      credentials: true
    }
  });
  
  // Authentifizierungs-Middleware
  io.use((socket, next) => {
    const token = socket.handshake.auth.token;
    
    // Prüfen, ob Token gültig ist (nur für Admin-Dashboard)
    if (!token || token !== process.env.SOCKET_AUTH_TOKEN) {
      return next(new Error('Nicht autorisiert'));
    }
    
    next();
  });
  
  // Verbindungshandler
  io.on('connection', (socket) => {
    console.log('Neue Socket.io-Verbindung:', socket.id);
    
    // Raum für Admin-Dashboard
    socket.join('admin');
    
    // Ereignis beim Trennen
    socket.on('disconnect', () => {
      console.log('Socket.io-Verbindung getrennt:', socket.id);
    });
    
    // Tenant-spezifischen Raum abonnieren
    socket.on('subscribe', (tenantId) => {
      if (typeof tenantId === 'string' && tenantId.match(/^[0-9]+$/)) {
        socket.join(`tenant-${tenantId}`);
        console.log(`Socket ${socket.id} abonniert Tenant ${tenantId}`);
      }
    });
    
    // Tenant-spezifischen Raum verlassen
    socket.on('unsubscribe', (tenantId) => {
      socket.leave(`tenant-${tenantId}`);
      console.log(`Socket ${socket.id} verlässt Tenant ${tenantId}`);
    });
  });
  
  // Event-Handler für Echtzeit-Updates
  eventEmitter.on(EventTypes.API_REQUEST_COMPLETED, (data) => {
    // An alle Verbindungen im Admin-Raum senden
    io.to('admin').emit('api_request', {
      type: 'api_request',
      timestamp: new Date().toISOString(),
      data: {
        tenant_id: data.tenantId,
        model: data.model,
        tokens: data.totalTokens,
        cost: data.cost
      }
    });
    
    // Auch an den spezifischen Tenant-Raum senden
    io.to(`tenant-${data.tenantId}`).emit('api_request', {
      type: 'api_request',
      timestamp: new Date().toISOString(),
      data: {
        model: data.model,
        tokens: data.totalTokens,
        cost: data.cost
      }
    });
  });
  
  // Statistik-Updates
  setInterval(async () => {
    try {
      // Aktuelle Systemstatistiken abrufen
      const stats = await getSystemStats();
      
      // An Admin-Dashboard senden
      io.to('admin').emit('system_stats', {
        type: 'system_stats',
        timestamp: new Date().toISOString(),
        data: stats
      });
    } catch (error) {
      console.error('Fehler beim Senden der Statistiken:', error);
    }
  }, 60000); // Jede Minute
  
  return io;
};

// Systemstatistiken abrufen
const getSystemStats = async () => {
  // Hier können Datenbank-Abfragen oder andere Datenquellen verwendet werden
  return {
    active_tenants: 0, // Anzahl aktiver Tenants
    requests_today: 0, // Anzahl der Anfragen heute
    tokens_today: 0, // Token-Verbrauch heute
    revenue_today: 0 // Umsatz heute
  };
};

module.exports = { initSocketServer };
```

### 16.5 API-Throttling mit Redis
- [ ] Erweiterte Rate-Limiting-Middleware mit Redis:
```javascript
// middleware/rate-limit-redis.js
const Redis = require('ioredis');
const { RateLimiterRedis } = require('rate-limiter-flexible');

// Redis-Verbindung initialisieren
const redisClient = new Redis(process.env.REDIS_URL);

// Standardlimits
const DEFAULT_POINTS = 100; // Anfragen pro Zeitraum
const DEFAULT_DURATION = 60; // Sekunden

// Limiter-Map für verschiedene Tenant-Typen
const limiters = {};

// Limiter für bestimmten Tenant initialisieren
const getLimiter = async (tenantId, customLimit = null) => {
  // Prüfen, ob Limiter bereits existiert
  if (limiters[tenantId]) {
    return limiters[tenantId];
  }
  
  // Benutzerdefinierte Limits verwenden oder Standardwerte
  const points = customLimit?.points || DEFAULT_POINTS;
  const duration = customLimit?.duration || DEFAULT_DURATION;
  
  // Neuen Limiter erstellen
  const limiter = new RateLimiterRedis({
    storeClient: redisClient,
    keyPrefix: `ratelimit:${tenantId}`,
    points,
    duration
  });
  
  // Limiter cachen
  limiters[tenantId] = limiter;
  
  return limiter;
};

// Rate-Limit-Middleware
const rateLimiterMiddleware = async (req, res, next) => {
  // Nur für API-Routen
  if (!req.path.startsWith('/api/')) {
    return next();
  }
  
  // Tenant-ID aus Request-Kontext
  const tenantId = req.tenantContext?.tenant?.id;
  
  if (!tenantId) {
    return next();
  }
  
  try {
    // Limiter abrufen (mit benutzerdefinierten Limits aus Tenant-Daten)
    const customLimit = req.tenantContext.tenant.rate_limit ? {
      points: req.tenantContext.tenant.rate_limit_per_minute || DEFAULT_POINTS,
      duration: 60
    } : null;
    
    const limiter = await getLimiter(tenantId, customLimit);
    
    // Anfrage gegen Rate-Limit prüfen
    const rateLimitResult = await limiter.consume(req.ip);
    
    // Headers für Rate-Limit setzen
    res.setHeader('X-RateLimit-Limit', rateLimitResult.limit);
    res.setHeader('X-RateLimit-Remaining', rateLimitResult.remainingPoints);
    res.setHeader('X-RateLimit-Reset', new Date(Date.now() + rateLimitResult.msBeforeNext).toISOString());
    
    next();
  } catch (error) {
    // Rate-Limit überschritten
    if (error.remainingPoints !== undefined) {
      res.setHeader('X-RateLimit-Limit', error.limit);
      res.setHeader('X-RateLimit-Remaining', 0);
      res.setHeader('X-RateLimit-Reset', new Date(Date.now() + error.msBeforeNext).toISOString());
      res.setHeader('Retry-After', Math.ceil(error.msBeforeNext / 1000));
      
      return res.status(429).json({
        error: 'Rate limit exceeded',
        message: `Zu viele Anfragen. Bitte warten Sie ${Math.ceil(error.msBeforeNext / 1000)} Sekunden.`,
        retryAfter: Math.ceil(error.msBeforeNext / 1000)
      });
    }
    
    // Sonstiger Fehler
    console.error('Rate-Limiter-Fehler:', error);
    next();
  }
};

module.exports = { rateLimiterMiddleware };
```

## 17. Implementierungszeitplan

### 17.1 Zeitplan für die Umsetzung
- [ ] Definieren Sie einen realistischen Zeitplan:

| Phase | Aufgabe | Geschätzte Zeit | Abhängigkeiten |
|-------|---------|-----------------|----------------|
| **1** | **Grundeinrichtung** | **3 Tage** | |
|       | Render.com-Setup | 0,5 Tage | - |
|       | PostgreSQL-Datenbank | 0,5 Tage | Render.com-Account |
|       | Projektstruktur | 1 Tag | - |
|       | Datenbankschema | 1 Tag | PostgreSQL |
| **2** | **Backend-Core** | **5 Tage** | |
|       | Express-Setup | 0,5 Tage | Projektstruktur |
|       | Subdomain-Routing | 1 Tag | - |
|       | OpenAI-Integration | 1 Tag | - |
|       | API-Authentifizierung | 1 Tag | Datenbankschema |
|       | Token-Tracking | 1,5 Tage | OpenAI-Integration |
| **3** | **Admin-Dashboard** | **7 Tage** | |
|       | Admin-Authentifizierung | 1 Tag | Datenbankschema |
|       | Dashboard-Views | 2 Tage | - |
|       | Kundenverwaltung | 1,5 Tage | - |
|       | Statistiken & Berichte | 1,5 Tage | Token-Tracking |
|       | Abrechnungssystem | 1 Tag | Statistiken |
| **4** | **Frontend-Styling** | **3 Tage** | |
|       | CSS-Framework | 0,5 Tage | - |
|       | Dashboard-Styling | 1,5 Tage | Dashboard-Views |
|       | Responsives Design | 1 Tag | CSS-Framework |
| **5** | **Deployment & Tests** | **2 Tage** | |
|       | CI/CD-Pipeline | 0,5 Tage | - |
|       | Deployment-Konfiguration | 0,5 Tage | Render.com-Setup |
|       | Integrationstest | 1 Tag | Backend-Core |
| **6** | **Dokumentation** | **2 Tage** | |
|       | API-Dokumentation | 0,5 Tage | Backend-Core |
|       | Admin-Handbuch | 0,5 Tage | Admin-Dashboard |
|       | Kundenanleitung | 0,5 Tage | - |
|       | Technische Dokumentation | 0,5 Tage | - |
| **7** | **Erweiterungen (optional)** | **4+ Tage** | |
|       | Mehrsprachigkeit | 1 Tag | - |
|       | Webhook-Integration | 1 Tag | - |
|       | Erweiterte Sicherheit | 1 Tag | - |
|       | Skalierungsvorbereitung | 1+ Tage | - |

**Geschätzte Gesamtdauer: 22-26 Arbeitstage**

### 17.2 Priorisierung
- [ ] Definieren Sie die Implementierungsprioritäten:

1. **Must-Have (Phase 1)**
   - Grundfunktionalität des Systems
   - Subdomain-Routing und API-Key-Authentifizierung
   - OpenAI-Integration mit Token-Tracking
   - Einfaches Admin-Dashboard

2. **Should-Have (Phase 2)**
   - Vollständiges Statistik- und Abrechnungssystem
   - Ansprechendes UI-Design
   - Dokumentation
   - Deployment-Pipeline

3. **Nice-to-Have (Phase 3)**
   - Erweiterte Sicherheitsmaßnahmen
   - Webhook-Integration
   - Mehrsprachigkeit
   - Echtzeit-Dashboard

4. **Future Enhancements (Phase 4)**
   - Skalierungsarchitektur
   - Mobile App für Administratoren
   - KI-basierte Anomalieerkennung
   - Erweiterte Analysen und Berichte

## 18. Projektabschluss und Übergabe

### 18.1 Abnahmekriterien
- [ ] Definieren Sie klare Abnahmekriterien:

1. **Funktionale Kriterien:**
   - Kunden können über ihre Subdomain auf die API zugreifen
   - API-Anfragen werden korrekt an OpenAI weitergeleitet
   - Token-Verbrauch wird genau erfasst und berechnet
   - Admin-Dashboard zeigt korrekte Statistiken
   - Abrechnungssystem erstellt korrekte Rechnungen
   - System läuft stabil auf Render.com

2. **Nicht-funktionale Kriterien:**
   - Antwortzeit der API unter 1 Sekunde (ohne OpenAI-Verarbeitungszeit)
   - Dashboard-Ladezeiten unter 2 Sekunden
   - System verarbeitet mindestens 10 gleichzeitige API-Anfragen
   - Dokumentation ist vollständig und verständlich
   - Code entspricht Best Practices und ist wartbar

3. **Sicherheitskriterien:**
   - API-Keys werden sicher gespeichert
   - Admin-Zugriff ist durch sichere Authentifizierung geschützt
   - Sensible Daten werden nur verschlüsselt übertragen
   - Kunden können nur auf ihre eigenen Daten zugreifen

### 18.2 Wartungsplan
- [ ] Erstellen Sie einen Wartungsplan:

1. **Regelmäßige Wartungsaufgaben:**
   - Tägliche Überprüfung der Logs auf Fehler
   - Wöchentliches Datenbank-Backup
   - Monatliche Überprüfung der OpenAI-Preise und Anpassung bei Bedarf
   - Quartalsweise Sicherheitsüberprüfung

2. **Überwachung:**
   - Einrichtung von Warnmeldungen für Systemfehler
   - Überwachung der Datenbankgröße und -performance
   - Überwachung der API-Verfügbarkeit

3. **Updates:**
   - Regelmäßige Aktualisierung der Abhängigkeiten
   - Implementierung neuer OpenAI-Features bei Bedarf
   - Sicherheitsupdates zeitnah einspielen

4. **Support:**
   - Definition von Support-Prozessen für Kundenanfragen
   - Dokumentation häufiger Probleme und Lösungen
   - Bereitstellung von Kontaktmöglichkeiten für Notfälle

### 18.3 Schulungsplan
- [ ] Planen Sie Schulungen für Administratoren:

1. **Admin-Schulung:**
   - Überblick über das System
   - Kundenverwaltung
   - Interpretation von Statistiken
   - Abrechnungsprozess
   - Troubleshooting

2. **Entwickler-Schulung:**
   - Architekturüberblick
   - Erweiterung des Systems
   - Deployment-Prozess
   - Datenbankschema

3. **Kundeneinweisung:**
   - API-Nutzung
   - Authentifizierung
   - Nutzungslimits
   - Abrechnungsmodell

### 18.4 Übergabe-Checkliste
- [ ] Erstellen Sie eine Übergabe-Checkliste:

- [ ] Quellcode vollständig auf GitHub
- [ ] Deployment auf Render.com eingerichtet
- [ ] Umgebungsvariablen dokumentiert
- [ ] Datenbankschema dokumentiert
- [ ] Admin-Zugangsdaten übergeben
- [ ] API-Dokumentation erstellt
- [ ] Benutzerhandbücher erstellt
- [ ] Support-Prozess definiert
- [ ] Wartungsplan übergeben
- [ ] Schulungen durchgeführt
- [ ] Backup-Strategie implementiert
- [ ] Lizenzinformationen übergeben## 12. Sicherheit und Best Practices

### 12.1 Verbesserte API-Sicherheit
- [ ] Implementierung von Helmet für verbesserte HTTP-Header-Sicherheit:
```javascript
// app.js (Erweiterung)
const helmet = require('helmet');

// Sicherheits-Header setzen
app.use(helmet());

// Content Security Policy anpassen
app.use(
  helmet.contentSecurityPolicy({
    directives: {
      defaultSrc: ["'self'"],
      scriptSrc: ["'self'", "https://cdn.jsdelivr.net", "https://cdnjs.cloudflare.com"],
      styleSrc: ["'self'", "'unsafe-inline'", "https://cdn.jsdelivr.net"],
      imgSrc: ["'self'", "data:"],
      connectSrc: ["'self'"],
      fontSrc: ["'self'", "https://cdn.jsdelivr.net"],
      objectSrc: ["'none'"],
      frameSrc: ["'none'"],
      upgradeInsecureRequests: []
    }
  })
);
```

- [ ] JWT-basierte Authentifizierung für API-Endpunkte:
```javascript
// middleware/jwt-auth.js
const jwt = require('jsonwebtoken');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

const JWT_SECRET = process.env.JWT_SECRET || 'your-jwt-secret-key';

// JWT-Token generieren
const generateToken = (customer) => {
  return jwt.sign(
    {
      customerId: customer.id,
      subdomain: customer.subdomain
    },
    JWT_SECRET,
    { expiresIn: '30d' } // Token ist 30 Tage gültig
  );
};

// Token validieren
const validateToken = (req, res, next) => {
  // Nur überprüfen, wenn Customer-Subdomain
  if (!req.tenantContext || !req.tenantContext.isSubdomain) {
    return next();
  }
  
  // API-Key hat Vorrang vor JWT
  if (req.headers['x-api-key']) {
    return next();
  }
  
  // Auth-Header extrahieren
  const authHeader = req.headers.authorization;
  
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({
      error: 'Unauthorized',
      message: 'API-Key oder gültiges Bearer-Token erforderlich'
    });
  }
  
  const token = authHeader.split(' ')[1];
  
  try {
    // Token verifizieren
    const decoded = jwt.verify(token, JWT_SECRET);
    
    // Prüfen, ob Token zu dieser Subdomain gehört
    if (decoded.subdomain !== req.tenantContext.tenant.subdomain) {
      return res.status(401).json({
        error: 'Unauthorized',
        message: 'Token gehört nicht zu dieser Subdomain'
      });
    }
    
    // Token-Informationen im Request speichern
    req.tokenData = decoded;
    next();
  } catch (error) {
    console.error('JWT-Validierungsfehler:', error);
    return res.status(401).json({
      error: 'Unauthorized',
      message: 'Ungültiges oder abgelaufenes Token'
    });
  }
};

// Token-Auth-Route
const createTokenAuth = async (req, res) => {
  try {
    const { api_key } = req.body;
    
    if (!api_key) {
      return res.status(400).json({
        error: 'Bad Request',
        message: 'API-Key ist erforderlich'
      });
    }
    
    // Kunde mit diesem API-Key suchen
    const result = await pool.query(
      'SELECT * FROM kunden WHERE api_key = $1 AND status = $2',
      [api_key, 'aktiv']
    );
    
    if (result.rows.length === 0) {
      return res.status(401).json({
        error: 'Unauthorized',
        message: 'Ungültiger API-Key'
      });
    }
    
    const customer = result.rows[0];
    
    // Token generieren
    const token = generateToken(customer);
    
    // API-Zugriff protokollieren
    await pool.query(
      `INSERT INTO apianfragen 
         (kunden_id, anfrage_typ, status, modell, total_tokens, kosten) 
       VALUES ($1, $2, $3, $4, $5, $6)`,
      [customer.id, 'token_auth', 'erfolg', 'auth', 0, 0]
    );
    
    res.json({
      token,
      expires_in: 2592000, // 30 Tage in Sekunden
      token_type: 'Bearer',
      customer: {
        id: customer.id,
        name: customer.name,
        subdomain: customer.subdomain
      }
    });
  } catch (error) {
    console.error('Token-Auth-Fehler:', error);
    res.status(500).json({
      error: 'Server Error',
      message: 'Fehler bei der Token-Generierung'
    });
  }
};

module.exports = {
  generateToken,
  validateToken,
  createTokenAuth
};
```

### 12.2 Verbesserte Datensicherheit
- [ ] API-Key-Verschlüsselung implementieren:
```javascript
// services/encryption.js
const crypto = require('crypto');

// Verschlüsselungsschlüssel aus Umgebungsvariable
const ENCRYPTION_KEY = process.env.ENCRYPTION_KEY || 'your-encryption-key-32-chars-long!';
const IV_LENGTH = 16; // Für AES, IV ist 16 Bytes

// Verschlüsseln
const encrypt = (text) => {
  const iv = crypto.randomBytes(IV_LENGTH);
  const cipher = crypto.createCipheriv(
    'aes-256-cbc',
    Buffer.from(ENCRYPTION_KEY),
    iv
  );
  
  let encrypted = cipher.update(text);
  encrypted = Buffer.concat([encrypted, cipher.final()]);
  
  return iv.toString('hex') + ':' + encrypted.toString('hex');
};

// Entschlüsseln
const decrypt = (text) => {
  const parts = text.split(':');
  const iv = Buffer.from(parts[0], 'hex');
  const encryptedText = Buffer.from(parts[1], 'hex');
  
  const decipher = crypto.createDecipheriv(
    'aes-256-cbc',
    Buffer.from(ENCRYPTION_KEY),
    iv
  );
  
  let decrypted = decipher.update(encryptedText);
  decrypted = Buffer.concat([decrypted, decipher.final()]);
  
  return decrypted.toString();
};

// API-Key generieren
const generateApiKey = () => {
  return crypto.randomBytes(32).toString('hex');
};

module.exports = {
  encrypt,
  decrypt,
  generateApiKey
};
```

- [ ] Sicherheitsprotokollierung implementieren:
```javascript
// middleware/security-logger.js
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

const securityLogger = async (req, res, next) => {
  // Original-URL speichern
  const originalUrl = req.originalUrl;
  const method = req.method;
  const ip = req.ip || req.headers['x-forwarded-for'] || req.socket.remoteAddress;
  const userAgent = req.headers['user-agent'];
  
  // Funktion zum Protokollieren von Sicherheitsereignissen
  req.logSecurityEvent = async (eventType, details, severity = 'info') => {
    try {
      await pool.query(
        `INSERT INTO security_events 
           (event_type, ip_address, user_agent, request_path, request_method, details, severity, tenant_id)
         VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
        [
          eventType,
          ip,
          userAgent,
          originalUrl,
          method,
          JSON.stringify(details),
          severity,
          req.tenantContext?.tenant?.id || null
        ]
      );
    } catch (error) {
      console.error('Fehler bei Sicherheitsprotokollierung:', error);
    }
  };
  
  // Response-Objekt erweitern, um Fehler zu protokollieren
  const originalSend = res.send;
  res.send = function(body) {
    // Bei 4xx/5xx-Fehlern protokollieren
    const statusCode = res.statusCode;
    if (statusCode >= 400) {
      let details = body;
      
      // JSON-Response parsen
      if (typeof body === 'string' && body.startsWith('{')) {
        try {
          details = JSON.parse(body);
        } catch (e) {
          // Ignorieren, wenn kein gültiges JSON
        }
      }
      
      // Fehler protokollieren
      const severity = statusCode >= 500 ? 'error' : 'warning';
      const eventType = statusCode >= 500 ? 'server_error' : 'client_error';
      
      req.logSecurityEvent(eventType, {
        statusCode,
        details
      }, severity);
    }
    
    originalSend.apply(res, arguments);
  };
  
  next();
};

module.exports = { securityLogger };
```

### 12.3 CORS und Zugriffsbeschränkungen
- [ ] Dynamische CORS-Konfiguration pro Tenant:
```javascript
// middleware/cors.js
const cors = require('cors');

const dynamicCors = (req, callback) => {
  // CORS-Einstellungen basierend auf Tenant
  if (req.tenantContext && req.tenantContext.tenant) {
    const tenant = req.tenantContext.tenant;
    
    // Erlaubte Origins aus den Tenant-Einstellungen lesen
    let allowedOrigins = [];
    
    if (tenant.allowed_origins) {
      // Kommaseparierte Liste in Array umwandeln
      allowedOrigins = tenant.allowed_origins.split(',').map(origin => origin.trim());
    }
    
    // Immer die eigene Subdomain erlauben
    const ownSubdomain = `https://${tenant.subdomain}.ihredomain.com`;
    if (!allowedOrigins.includes(ownSubdomain)) {
      allowedOrigins.push(ownSubdomain);
    }
    
    // Origin aus dem Request
    const origin = req.header('Origin');
    
    // CORS-Optionen
    const corsOptions = {
      origin: allowedOrigins.includes(origin) ? origin : false,
      methods: ['GET', 'POST', 'OPTIONS'],
      allowedHeaders: ['Content-Type', 'Authorization', 'X-API-Key'],
      maxAge: 86400 // 24 Stunden
    };
    
    callback(null, corsOptions);
  } else {
    // Standard CORS-Einstellungen für nicht-Tenant-Anfragen
    callback(null, {
      origin: false, // Keine CORS-Anfragen erlauben
      methods: ['GET', 'POST', 'OPTIONS']
    });
  }
};

// CORS-Middleware mit dynamischer Konfiguration
const setupCors = () => {
  return cors(dynamicCors);
};

module.exports = { setupCors };
```

### 12.4 Verbesserte Fehlerbehandlung
- [ ] Zentralisierte Fehlerbehandlung:
```javascript
// middleware/error-handler.js
const { logError } = require('../services/logger');

// 404-Handler
const notFoundHandler = (req, res, next) => {
  const error = new Error(`Die angeforderte URL ${req.originalUrl} wurde nicht gefunden`);
  error.status = 404;
  next(error);
};

// Globaler Fehlerhandler
const errorHandler = (err, req, res, next) => {
  // Fehlerinformationen
  const status = err.status || 500;
  const message = err.message || 'Ein unerwarteter Fehler ist aufgetreten';
  const stack = process.env.NODE_ENV === 'production' ? undefined : err.stack;
  
  // Fehler protokollieren
  logError({
    status,
    message,
    stack,
    url: req.originalUrl,
    method: req.method,
    ip: req.ip,
    tenant: req.tenantContext?.tenant?.subdomain || 'main',
    tenantId: req.tenantContext?.tenant?.id || null
  });
  
  // Sicherheitsereignis protokollieren für schwerwiegende Fehler
  if (status >= 500 && req.logSecurityEvent) {
    req.logSecurityEvent('server_error', {
      status,
      message,
      url: req.originalUrl
    }, 'error');
  }
  
  // Fehlermeldung formatieren basierend auf Accept-Header
  if (req.accepts('html')) {
    // HTML-Antwort für Browser
    res.status(status).render('error', {
      title: `Fehler ${status}`,
      status,
      message,
      stack: process.env.NODE_ENV === 'production' ? null : stack
    });
  } else {
    // JSON-Antwort für API-Anfragen
    res.status(status).json({
      error: {
        code: status,
        message,
        ...(process.env.NODE_ENV !== 'production' && stack ? { stack } : {})
      }
    });
  }
};

module.exports = {
  notFoundHandler,
  errorHandler
};
```

### 12.5 Performance-Optimierungen
- [ ] Caching-Strategie implementieren:
```javascript
// middleware/cache.js
const NodeCache = require('node-cache');
const crypto = require('crypto');

// Cache-Instanz mit 5 Minuten TTL
const cache = new NodeCache({ stdTTL: 300 });

// Cache-Key-Generator
const generateCacheKey = (req) => {
  const tenantId = req.tenantContext?.tenant?.id || 'main';
  const path = req.originalUrl;
  const method = req.method;
  
  // Für POST-Anfragen auch den Body berücksichtigen
  let bodyHash = '';
  if (method === 'POST' && req.body) {
    const bodyStr = JSON.stringify(req.body);
    bodyHash = crypto.createHash('md5').update(bodyStr).digest('hex');
  }
  
  return `${tenantId}:${method}:${path}:${bodyHash}`;
};

// Cache-Middleware
const cacheMiddleware = (duration = 300) => {
  return (req, res, next) => {
    // Nur GET- und POST-Anfragen cachen
    if (req.method !== 'GET' && req.method !== 'POST') {
      return next();
    }
    
    // Cache-Key generieren
    const key = generateCacheKey(req);
    
    // Im Cache suchen
    const cachedResponse = cache.get(key);
    
    if (cachedResponse) {
      // Cache-Hit
      res.set('X-Cache', 'HIT');
      return res.status(cachedResponse.status)
        .set(cachedResponse.headers)
        .send(cachedResponse.data);
    }
    
    // Cache-Miss, Original-Response-Methoden speichern
    res.set('X-Cache', 'MISS');
    const originalSend = res.send;
    
    // Response-Objekt erweitern
    res.send = function(body) {
      // Nur erfolgreiche Responses cachen
      if (res.statusCode < 400) {
        // Response im Cache speichern
        cache.set(key, {
          status: res.statusCode,
          headers: res.getHeaders(),
          data: body
        }, duration);
      }
      
      // Original-Funktion aufrufen
      originalSend.call(this, body);
    };
    
    next();
  };
};

// Cache löschen
const clearCache = (pattern) => {
  if (pattern) {
    // Alle passenden Keys finden und löschen
    const keys = cache.keys();
    const matchingKeys = keys.filter(key => key.includes(pattern));
    matchingKeys.forEach(key => cache.del(key));
    return matchingKeys.length;
  } else {
    // Gesamten Cache leeren
    cache.flushAll();
    return 'all';
  }
};

module.exports = {
  cacheMiddleware,
  clearCache
};
```

## 13. Erweiterte Entwicklungs- und Deployment-Tools

### 13.1 Docker-Container für Entwicklung
- [ ] Docker-Compose-Setup für lokale Entwicklung:
```yaml
# docker-compose.yml
version: '3.8'

services:
  # PostgreSQL-Datenbank
  postgres:
    image: postgres:14
    environment:
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_USER=postgres
      - POSTGRES_DB=pim_connector
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 5s
      timeout: 5s
      retries: 5

  # Node.js-Anwendung
  app:
    build:
      context: .
      dockerfile: Dockerfile.dev
    volumes:
      - .:/app
      - /app/node_modules
    ports:
      - "3000:3000"
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      - NODE_ENV=development
      - DATABASE_URL=postgres://postgres:postgres@postgres:5432/pim_connector
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - SESSION_SECRET=dev-session-secret
      - ENCRYPTION_KEY=dev-encryption-key-32-chars-long!
      - PORT=3000
    command: npm run dev

volumes:
  postgres_data:
```

- [ ] Dockerfile für Entwicklung:
```dockerfile
# Dockerfile.dev
FROM node:18-alpine

WORKDIR /app

# Abhängigkeiten installieren
COPY package*.json ./
RUN npm install

# Quellcode kopieren
COPY . .

# Beim Start Datenbank-Migration ausführen
CMD ["sh", "-c", "npm run migrate && npm run dev"]
```

### 13.2 CI/CD-Pipeline mit GitHub Actions
- [ ] GitHub-Actions-Workflow-Datei erstellen:
```yaml
# .github/workflows/ci-cd.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:14
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_USER: postgres
          POSTGRES_DB: pim_connector_test
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
          cache: 'npm'
      
      - name: Install dependencies
        run: npm ci
      
      - name: Run linter
        run: npm run lint
      
      - name: Run tests
        run: npm test
        env:
          DATABASE_URL: postgres://postgres:postgres@localhost:5432/pim_connector_test
          NODE_ENV: test
          SESSION_SECRET: test-session-secret
          ENCRYPTION_KEY: test-encryption-key-32-chars-long!
  
  deploy:
    needs: test
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Deploy to Render
        run: |
          curl -X POST ${{ secrets.RENDER_DEPLOY_HOOK }}
```

### 13.3 Monitoring und Logging
- [ ] Zentralisierte Logging-Lösung:
```javascript
// services/logger.js
const winston = require('winston');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Winston-Logger konfigurieren
const logger = winston.createLogger({
  level: process.env.LOG_LEVEL || 'info',
  format: winston.format.combine(
    winston.format.timestamp(),
    winston.format.json()
  ),
  defaultMeta: { service: 'pim-connector' },
  transports: [
    // Konsolenausgabe für Entwicklung
    new winston.transports.Console({
      format: winston.format.combine(
        winston.format.colorize(),
        winston.format.simple()
      )
    })
  ]
});

// In Produktion auch in Datei schreiben
if (process.env.NODE_ENV === 'production') {
  logger.add(new winston.transports.File({
    filename: 'logs/error.log',
    level: 'error',
    maxsize: 5242880, // 5MB
    maxFiles: 5
  }));
  
  logger.add(new winston.transports.File({
    filename: 'logs/combined.log',
    maxsize: 5242880, // 5MB
    maxFiles: 5
  }));
}

// API-Anfragen protokollieren
const logApiRequest = async (req, res, responseTime) => {
  try {
    const tenantId = req.tenantContext?.tenant?.id;
    
    // Nur API-Anfragen protokollieren
    if (!tenantId || !req.originalUrl.startsWith('/api/')) {
      return;
    }
    
    // Basis-Informationen
    const logData = {
      method: req.method,
      path: req.originalUrl,
      statusCode: res.statusCode,
      responseTime,
      ip: req.ip || req.headers['x-forwarded-for'],
      userAgent: req.headers['user-agent'],
      tenantId
    };
    
    // In Logger und Datenbank schreiben
    logger.info('API Request', logData);
    
    // Auch in Datenbank speichern für kritische Anfragen
    if (res.statusCode >= 400 || responseTime > 1000) {
      await pool.query(
        `INSERT INTO api_logs 
           (tenant_id, request_path, request_method, status_code, response_time, ip_address, user_agent)
         VALUES ($1, $2, $3, $4, $5, $6, $7)`,
        [
          tenantId,
          req.originalUrl,
          req.method,
          res.statusCode,
          responseTime,
          logData.ip,
          logData.userAgent
        ]
      );
    }
  } catch (error) {
    console.error('Fehler beim Protokollieren der API-Anfrage:', error);
  }
};

// Fehler protokollieren
const logError = async (errorData) => {
  try {
    // In Logger schreiben
    logger.error('Application Error', errorData);
    
    // In Datenbank speichern
    if (errorData.tenantId) {
      await pool.query(
        `INSERT INTO error_logs 
           (tenant_id, error_message, error_stack, request_path, request_method, status_code, ip_address)
         VALUES ($1, $2, $3, $4, $5, $6, $7)`,
        [
          errorData.tenantId,
          errorData.message,
          errorData.stack,
          errorData.url,
          errorData.method,
          errorData.status,
          errorData.ip
        ]
      );
    }
  } catch (dbError) {
    console.error('Fehler beim Speichern des Fehlers in der Datenbank:', dbError);
  }
};

module.exports = {
  logger,
  logApiRequest,
  logError
};
```

- [ ] Health-Check-Endpunkt:
```javascript
// routes/health.js
const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });
const os = require('os');

// Basis-Health-Check
router.get('/', (req, res) => {
  res.status(200).json({
    status: 'ok',
    timestamp: new Date().toISOString()
  });
});

// Ausführlicher Health-Check
router.get('/detailed', async (req, res) => {
  try {
    // Datenbankverbindung prüfen
    const dbStart = Date.now();
    const dbResult = await pool.query('SELECT NOW()');
    const dbResponseTime = Date.now() - dbStart;
    
    // Systemressourcen
    const memoryUsage = process.memoryUsage();
    const uptime = process.uptime();
    const loadAvg = os.loadavg();
    const freeMem = os.freemem();
    const totalMem = os.totalmem();
    
    res.status(200).json({
      status: 'ok',
      timestamp: new Date().toISOString(),
      version: process.env.npm_package_version || 'unknown',
      uptime: {
        seconds: uptime,
        formatted: formatUptime(uptime)
      },
      database: {
        connected: true,
        responseTime: `${dbResponseTime}ms`,
        timestamp: dbResult.rows[0].now
      },
      system: {
        platform: process.platform,
        nodeVersion: process.version,
        memoryUsage: {
          rss: formatBytes(memoryUsage.rss),
          heapTotal: formatBytes(memoryUsage.heapTotal),
          heapUsed: formatBytes(memoryUsage.heapUsed),
          external: formatBytes(memoryUsage.external)
        },
        systemMemory: {
          total: formatBytes(totalMem),
          free: formatBytes(freeMem),
          used: formatBytes(totalMem - freeMem),
          usedPercentage: ((totalMem - freeMem) / totalMem * 100).toFixed(2) + '%'
        },
        loadAverage: loadAvg
      }
    });
  } catch (error) {
    console.error('Health-Check-Fehler:', error);
    
    res.status(500).json({
      status: 'error',
      timestamp: new Date().toISOString(),
      error: error.message
    });
  }
});

// Hilfsfunktionen
function formatUptime(seconds) {
  const days = Math.floor(seconds / 86400);
  seconds %= 86400;
  const hours = Math.floor(seconds / 3600);
  seconds %= 3600;
  const minutes = Math.floor(seconds / 60);
  seconds = Math.floor(seconds % 60);
  
  return `${days}d ${hours}h ${minutes}m ${seconds}s`;
}

function formatBytes(bytes) {
  if (bytes === 0) return '0 Bytes';
  
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

module.exports = router;
```

## 14. Erweiterter Dokumentationsplan

### 14.1 API-Dokumentation
- [ ] OpenAPI/Swagger-Dokumentation:
```javascript
// routes/api-docs.js
const express = require('express');
const router = express.Router();
const swaggerJsdoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');

// Swagger-Definition
const swaggerOptions = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'PIM AI Connector API',
      version: '1.0.0',
      description: 'API-Dokumentation für den PIM AI Connector'
    },
    servers: [
      {
        url: 'https://{tenant}.ihredomain.com',
        variables: {
          tenant: {
            default: 'api',
            description: 'Subdomain des Kunden'
          }
        }
      }
    ],
    components: {
      securitySchemes: {
        ApiKeyAuth: {
          type: 'apiKey',
          in: 'header',
          name: 'X-API-Key'
        },
        BearerAuth: {
          type: 'http',
          scheme: 'bearer',
          bearerFormat: 'JWT'
        }
      }
    },
    security: [
      { ApiKeyAuth: [] },
      { BearerAuth: [] }
    ]
  },
  apis: ['./routes/api.js', './docs/*.yaml']
};

const swaggerSpec = swaggerJsdoc(swaggerOptions);

// Swagger-UI-Setup
router.use('/', swaggerUi.serve);
router.get('/', swaggerUi.setup(swaggerSpec, {
  explorer: true,
  customCss: '.swagger-ui .topbar { display: none }'
}));

// Raw-JSON-Export
router.get('/json', (req, res) => {
  res.setHeader('Content-Type', 'application/json');
  res.send(swaggerSpec);
});

module.exports = router;
```

- [ ] Beispiel-API-Dokumentation:
```yaml
# docs/api-chat.yaml
paths:
  /api/completion:
    post:
      summary: Erstellt eine Chat-Completion mit dem angegebenen Modell
      tags:
        - Chat
      security:
        - ApiKeyAuth: []
        - BearerAuth: []
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required:
                - messages
              properties:
                model:
                  type: string
                  description: Das zu verwendende OpenAI-Modell
                  example: gpt-3.5-turbo
                  default: gpt-3.5-turbo
                messages:
                  type: array
                  description: Die Nachrichten im Chat
                  items:
                    type: object
                    required:
                      - role
                      - content
                    properties:
                      role:
                        type: string
                        enum: [system,## 12. Sicher# TODO: PIM-AI-Connector System mit Kundenverwaltung

## Systemarchitektur

Das System basiert auf dem [pim-ai-connector-lite](https://github.com/thanhtuanh/pim-ai-connector-lite/) Repository und wird um Kundenverwaltung, Token-Berechnung und Admin-Dashboard erweitert. Deployment erfolgt auf Render.com.

### Kernkomponenten:
- PostgreSQL Datenbank
- Node.js Backend (Express)
- Integriertes Admin-Dashboard
- OpenAI API Integration
- Subdomain-Routing für Kunden

## 1. Grundeinrichtung

### 1.1 Render.com Setup
- [ ] Render.com Account erstellen
- [ ] PostgreSQL Datenbank einrichten (1GB Starter)
  - [ ] Verbindungsdetails sichern
  - [ ] Datenbank-Backup-Strategie definieren
- [ ] Web Service für Node.js App einrichten
  - [ ] GitHub Repository verbinden
  - [ ] Build-Befehl konfigurieren: `npm install`
  - [ ] Start-Befehl konfigurieren: `npm start`
  - [ ] Umgebungsvariablen setzen:
    - [ ] `DATABASE_URL`
    - [ ] `OPENAI_API_KEY`
    - [ ] `JWT_SECRET`
    - [ ] `ADMIN_USER`
    - [ ] `ADMIN_PASSWORD`

### 1.2 Domain-Setup
- [ ] Hauptdomain bei Domain-Provider konfigurieren
- [ ] Wildcard-SSL-Zertifikat für *.ihredomain.com einrichten
- [ ] DNS konfigurieren:
  - [ ] A-Record für Hauptdomain
  - [ ] CNAME für Wildcard-Subdomains zu Render.com
- [ ] Custom Domains in Render.com einrichten

## 2. Datenbankschema

### 2.1 Datenbank-Tabellen erstellen
- [ ] SQL-Skript für initiale Datenbankstruktur erstellen:

```sql
-- Kundentabelle
CREATE TABLE kunden (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(100) NOT NULL UNIQUE,
    api_key VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) DEFAULT 'aktiv',
    max_tokens_pro_monat INTEGER DEFAULT 0,
    erstellungsdatum TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    kontakt_email VARCHAR(255)
);

-- API-Anfragen-Tabelle
CREATE TABLE apianfragen (
    id SERIAL PRIMARY KEY,
    kunden_id INTEGER REFERENCES kunden(id),
    zeitstempel TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    anfrage_typ VARCHAR(100) NOT NULL,
    prompt_tokens INTEGER DEFAULT 0,
    completion_tokens INTEGER DEFAULT 0,
    total_tokens INTEGER DEFAULT 0,
    kosten DECIMAL(10, 6) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'erfolg',
    modell VARCHAR(100),
    fehler TEXT
);

-- Abrechungen-Tabelle
CREATE TABLE abrechnungen (
    id SERIAL PRIMARY KEY,
    kunden_id INTEGER REFERENCES kunden(id),
    monat INTEGER NOT NULL,
    jahr INTEGER NOT NULL,
    gesamttokens INTEGER DEFAULT 0,
    gesamtkosten DECIMAL(10, 2) DEFAULT 0,
    bezahlstatus VARCHAR(50) DEFAULT 'offen',
    erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    bezahlt_am TIMESTAMP,
    UNIQUE(kunden_id, monat, jahr)
);

-- Admin-Benutzer-Tabelle
CREATE TABLE admin_benutzer (
    id SERIAL PRIMARY KEY,
    benutzername VARCHAR(100) NOT NULL UNIQUE,
    passwort_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    letzter_login TIMESTAMP
);

-- Initiale Admin-Anmeldedaten einfügen (Passwort muss gehasht werden)
INSERT INTO admin_benutzer (benutzername, passwort_hash, name)
VALUES ('admin', '[HASH_HIER_EINFÜGEN]', 'Administrator');
```

- [ ] Skript zur Datenbank-Migration erstellen
- [ ] Testdaten für Entwicklung erstellen

## 3. Backend-Implementierung

### 3.1 Projektstruktur einrichten
- [ ] pim-ai-connector-lite klonen & erweitern
- [ ] Projektstruktur organisieren:
```
/
├── config/                # Konfigurationsdateien
│   ├── database.js        # Datenbankverbindung
│   ├── openai.js          # OpenAI Konfiguration
│   └── auth.js            # Authentifizierung
├── models/                # Datenmodelle
│   ├── Customer.js        # Kundenmodell
│   ├── ApiRequest.js      # API-Anfragenmodell
│   └── Invoice.js         # Abrechnungsmodell
├── routes/                # API-Routen
│   ├── api.js             # Kunde API-Endpunkte
│   ├── admin.js           # Admin-Routen
│   └── auth.js            # Authentifizierungsrouten
├── middleware/            # Express Middleware
│   ├── auth.js            # Authentifizierungsprüfung
│   ├── customer.js        # Kunden-Identifikation
│   └── logger.js          # API-Anfragen-Logging
├── services/              # Geschäftslogik
│   ├── openai.js          # OpenAI API-Integration
│   ├── billing.js         # Abrechnungsservice
│   └── tokenCounter.js    # Token-Berechnungen
├── views/                 # Admin-Dashboard Views
│   ├── partials/          # Wiederverwendbare UI-Teile
│   ├── admin/             # Admin-Dashboards
│   └── auth/              # Login-Seiten
├── public/                # Statische Dateien
│   ├── css/               # Stylesheets
│   ├── js/                # Client-Javascript
│   └── images/            # Bilder/Icons
├── scripts/               # Hilfsskripte
│   ├── db-migrate.js      # Datenbank-Migration
│   └── generate-invoice.js # Rechnungsgenerierung
├── app.js                 # Express App
└── server.js              # Server-Einstiegspunkt
```

### 3.2 Basis-API-Implementierung
- [ ] Express-App erweitern
- [ ] Datenbankverbindung einrichten
- [ ] OpenAI-Clientkonfiguration
- [ ] Middleware für Kunden-Identifikation:
```javascript
// middleware/customer.js
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

const identifyCustomerBySubdomain = async (req, res, next) => {
  try {
    // Host aus Header extrahieren
    const host = req.headers.host;
    // Subdomain extrahieren (erstes Segment)
    const subdomain = host.split('.')[0];
    
    // Keine Subdomain oder "www" => Hauptseite oder Admin-Dashboard
    if (!subdomain || subdomain === 'www' || subdomain === 'admin') {
      req.isMainDomain = true;
      return next();
    }
    
    // Kunde in Datenbank suchen
    const result = await pool.query(
      'SELECT * FROM kunden WHERE subdomain = $1',
      [subdomain]
    );
    
    // Kunde nicht gefunden
    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Kunde nicht gefunden' });
    }
    
    // Kunde gefunden, in Request speichern
    req.customer = result.rows[0];
    req.isCustomerSubdomain = true;
    
    next();
  } catch (error) {
    console.error('Fehler bei Kundenidentifikation:', error);
    res.status(500).json({ error: 'Server-Fehler' });
  }
};

module.exports = { identifyCustomerBySubdomain };
```

- [ ] Middleware für API-Key-Validierung:
```javascript
// middleware/auth.js
const validateApiKey = async (req, res, next) => {
  try {
    // Kein Customer = keine API-Validierung nötig (Admin-Bereich)
    if (!req.isCustomerSubdomain) {
      return next();
    }
    
    // API-Key aus Header lesen
    const apiKey = req.headers['x-api-key'];
    
    // Kein API-Key
    if (!apiKey) {
      return res.status(401).json({ error: 'API-Key erforderlich' });
    }
    
    // API-Key validieren
    if (apiKey !== req.customer.api_key) {
      return res.status(401).json({ error: 'Ungültiger API-Key' });
    }
    
    // API-Key gültig
    next();
  } catch (error) {
    console.error('Fehler bei API-Key-Validierung:', error);
    res.status(500).json({ error: 'Server-Fehler' });
  }
};

module.exports = { validateApiKey };
```

### 3.3 OpenAI-Integration mit Token-Tracking
- [ ] Service für OpenAI-Anfragen erstellen:
```javascript
// services/openai.js
const { Configuration, OpenAIApi } = require('openai');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// OpenAI-Client initialisieren
const configuration = new Configuration({ apiKey: process.env.OPENAI_API_KEY });
const openai = new OpenAIApi(configuration);

// Token-Preise pro 1K Tokens
const TOKEN_PRICES = {
  'gpt-3.5-turbo': { prompt: 0.0015, completion: 0.002 },
  'gpt-4': { prompt: 0.03, completion: 0.06 },
  // Weitere Modelle hier hinzufügen
};

// Kosten berechnen
const calculateCost = (model, promptTokens, completionTokens) => {
  const modelRates = TOKEN_PRICES[model] || TOKEN_PRICES['gpt-3.5-turbo'];
  return (
    (promptTokens * modelRates.prompt + completionTokens * modelRates.completion) / 1000
  );
};

// Anfrage protokollieren
const logApiRequest = async (customerId, requestType, model, usage, status, error = null) => {
  try {
    const { prompt_tokens, completion_tokens, total_tokens } = usage || { prompt_tokens: 0, completion_tokens: 0, total_tokens: 0 };
    const cost = calculateCost(model, prompt_tokens, completion_tokens);
    
    await pool.query(
      `INSERT INTO apianfragen 
        (kunden_id, anfrage_typ, modell, prompt_tokens, completion_tokens, total_tokens, kosten, status, fehler) 
       VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`,
      [customerId, requestType, model, prompt_tokens, completion_tokens, total_tokens, cost, status, error]
    );
  } catch (dbError) {
    console.error('Fehler beim Protokollieren der API-Anfrage:', dbError);
  }
};

// Chat Completion Anfrage senden
const createChatCompletion = async (customerId, requestBody) => {
  try {
    const model = requestBody.model || 'gpt-3.5-turbo';
    
    // Prüfen, ob Kunde das Limit überschritten hat
    const limitCheck = await checkCustomerLimits(customerId);
    if (!limitCheck.allowed) {
      return { error: limitCheck.message };
    }
    
    // OpenAI-Anfrage senden
    const response = await openai.createChatCompletion(requestBody);
    
    // Erfolgreiche Anfrage protokollieren
    await logApiRequest(
      customerId,
      'chat_completion',
      model,
      response.data.usage,
      'erfolg'
    );
    
    // Erfolgreiche Antwort
    return { success: true, data: response.data };
  } catch (error) {
    // Fehlerhafte Anfrage protokollieren
    await logApiRequest(
      customerId,
      'chat_completion',
      requestBody.model || 'gpt-3.5-turbo',
      null,
      'fehler',
      error.message
    );
    
    // Fehlerantwort
    return { 
      success: false, 
      error: error.message, 
      details: error.response?.data || {} 
    };
  }
};

// Kundenlimits prüfen
const checkCustomerLimits = async (customerId) => {
  try {
    // Kunde aus DB laden
    const customerResult = await pool.query(
      'SELECT max_tokens_pro_monat FROM kunden WHERE id = $1',
      [customerId]
    );
    
    if (customerResult.rows.length === 0) {
      return { allowed: false, message: 'Kunde nicht gefunden' };
    }
    
    const customer = customerResult.rows[0];
    
    // Wenn kein Limit gesetzt, immer erlauben
    if (!customer.max_tokens_pro_monat || customer.max_tokens_pro_monat === 0) {
      return { allowed: true };
    }
    
    // Aktuellen Monatsverbrauch ermitteln
    const currentMonth = new Date().getMonth() + 1; // 1-12
    const currentYear = new Date().getFullYear();
    
    const usageResult = await pool.query(
      `SELECT SUM(total_tokens) as monthly_usage 
       FROM apianfragen 
       WHERE kunden_id = $1 AND zeitstempel >= CURRENT_DATE - INTERVAL '30 days'
       GROUP BY tag
       ORDER BY tag`,
      [id]
    );
    
    // Anfragen-Historie
    const anfragenResult = await pool.query(
      `SELECT * FROM apianfragen
       WHERE kunden_id = $1
       ORDER BY zeitstempel DESC
       LIMIT 100`,
      [id]
    );
    
    // Chart-Daten formatieren
    const labels = [];
    const tokenData = [];
    
    nutzungResult.rows.forEach(row => {
      labels.push(new Date(row.tag).toLocaleDateString());
      tokenData.push(row.tokens);
    });
    
    res.render('admin/kunden/detail', {
      title: `Kunde: ${kunde.name}`,
      kunde,
      nutzung: nutzungResult.rows,
      anfragen: anfragenResult.rows,
      chartDaten: {
        labels,
        tokens: tokenData
      }
    });
  } catch (error) {
    console.error('Kundendetails-Fehler:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Neuen Kunden anlegen (Formular)
router.get('/kunden/neu', isAdmin, (req, res) => {
  res.render('admin/kunden/neu', {
    title: 'Neuen Kunden anlegen'
  });
});

// Neuen Kunden anlegen (Verarbeitung)
router.post('/kunden/neu', isAdmin, async (req, res) => {
  try {
    const { name, email, subdomain, max_tokens } = req.body;
    
    // API-Key generieren
    const apiKey = crypto.randomBytes(24).toString('hex');
    
    // Neuen Kunden in DB anlegen
    const result = await pool.query(
      `INSERT INTO kunden (name, kontakt_email, subdomain, api_key, max_tokens_pro_monat)
       VALUES ($1, $2, $3, $4, $5)
       RETURNING id`,
      [name, email, subdomain, apiKey, max_tokens || 0]
    );
    
    // Zur Kundendetailseite weiterleiten
    res.redirect(`/admin/kunden/${result.rows[0].id}`);
  } catch (error) {
    console.error('Fehler beim Anlegen des Kunden:', error);
    res.render('admin/kunden/neu', {
      title: 'Neuen Kunden anlegen',
      error: 'Fehler beim Anlegen des Kunden. Bitte versuchen Sie es erneut.',
      formData: req.body
    });
  }
});

// Kundenstatistiken und Abrechnung
router.get('/abrechnung', isAdmin, async (req, res) => {
  try {
    // Aktueller Monat und Jahr
    const heute = new Date();
    const aktuellerMonat = heute.getMonth() + 1; // 1-12
    const aktuellesJahr = heute.getFullYear();
    
    // Monatsabrechnung für alle Kunden
    const abrechnungResult = await pool.query(
      `SELECT 
        k.id as kunden_id,
        k.name as kundenname,
        SUM(a.total_tokens) as tokens,
        SUM(a.kosten) as kosten
       FROM kunden k
       LEFT JOIN apianfragen a ON k.id = a.kunden_id
       WHERE EXTRACT(MONTH FROM a.zeitstempel) = $1
       AND EXTRACT(YEAR FROM a.zeitstempel) = $2
       GROUP BY k.id, k.name
       ORDER BY kosten DESC`,
      [aktuellerMonat, aktuellesJahr]
    );
    
    // Abrechnungsstatus
    const statusResult = await pool.query(
      `SELECT * FROM abrechnungen
       WHERE monat = $1 AND jahr = $2`,
      [aktuellerMonat, aktuellesJahr]
    );
    
    // Abrechnungsstatus in Kundendaten integrieren
    const kundenAbrechnung = abrechnungResult.rows.map(kunde => {
      const abrechnungStatus = statusResult.rows.find(a => a.kunden_id === kunde.kunden_id) || {};
      return {
        ...kunde,
        abrechnung_id: abrechnungStatus.id || null,
        bezahlstatus: abrechnungStatus.bezahlstatus || 'offen'
      };
    });
    
    res.render('admin/abrechnung', {
      title: 'Abrechnung',
      monat: aktuellerMonat,
      jahr: aktuellesJahr,
      kunden: kundenAbrechnung
    });
  } catch (error) {
    console.error('Abrechnungs-Fehler:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Abrechnung generieren
router.post('/abrechnung/generieren', isAdmin, async (req, res) => {
  try {
    const { monat, jahr, kunden_ids } = req.body;
    
    // Für jeden ausgewählten Kunden eine Abrechnung erstellen
    for (const kundenId of kunden_ids) {
      // Nutzungsstatistik für den Monat abrufen
      const nutzungResult = await pool.query(
        `SELECT 
          SUM(total_tokens) as tokens,
          SUM(kosten) as kosten
         FROM apianfragen
         WHERE kunden_id = $1
         AND EXTRACT(MONTH FROM zeitstempel) = $2
         AND EXTRACT(YEAR FROM zeitstempel) = $3`,
        [kundenId, monat, jahr]
      );
      
      const { tokens, kosten } = nutzungResult.rows[0];
      
      // Prüfen, ob bereits eine Abrechnung existiert
      const existingResult = await pool.query(
        `SELECT id FROM abrechnungen
         WHERE kunden_id = $1 AND monat = $2 AND jahr = $3`,
        [kundenId, monat, jahr]
      );
      
      if (existingResult.rows.length > 0) {
        // Bestehende Abrechnung aktualisieren
        await pool.query(
          `UPDATE abrechnungen
           SET gesamttokens = $1, gesamtkosten = $2
           WHERE id = $3`,
          [tokens || 0, kosten || 0, existingResult.rows[0].id]
        );
      } else {
        // Neue Abrechnung erstellen
        await pool.query(
          `INSERT INTO abrechnungen (kunden_id, monat, jahr, gesamttokens, gesamtkosten)
           VALUES ($1, $2, $3, $4, $5)`,
          [kundenId, monat, jahr, tokens || 0, kosten || 0]
        );
      }
    }
    
    res.redirect('/admin/abrechnung');
  } catch (error) {
    console.error('Fehler bei der Abrechnungsgenerierung:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Bezahlstatus ändern
router.post('/abrechnung/status', isAdmin, async (req, res) => {
  try {
    const { abrechnung_id, status } = req.body;
    
    await pool.query(
      `UPDATE abrechnungen
       SET bezahlstatus = $1, 
           bezahlt_am = $2
       WHERE id = $3`,
      [status, status === 'bezahlt' ? 'CURRENT_TIMESTAMP' : null, abrechnung_id]
    );
    
    res.redirect('/admin/abrechnung');
  } catch (error) {
    console.error('Fehler beim Aktualisieren des Bezahlstatus:', error);
    res.status(500).send('Server-Fehler');
  }
});

module.exports = router;
```

## 11. Erweiterte Funktionen

### 11.1 Multi-Tenant-Architektur verbessern
- [ ] Implementieren Sie eine robustere Multi-Tenant-Lösung:
```javascript
// middleware/tenancy.js
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Tenant-Kontext für Requests
const tenantContext = async (req, res, next) => {
  try {
    // Host-Header extrahieren und Subdomain identifizieren
    const host = req.headers.host || '';
    const baseDomain = process.env.BASE_DOMAIN || 'ihredomain.com';
    
    // Prüfen, ob es sich um eine Subdomain handelt
    if (host === baseDomain || host === `www.${baseDomain}`) {
      req.tenantContext = {
        isSubdomain: false,
        isMainDomain: true
      };
      return next();
    }
    
    // Subdomain extrahieren
    const subdomain = host.split('.')[0];
    
    // Tenant in Datenbank suchen
    const result = await pool.query(
      'SELECT * FROM kunden WHERE subdomain = $1 AND status = $2',
      [subdomain, 'aktiv']
    );
    
    if (result.rows.length === 0) {
      // Keine Übereinstimmung gefunden
      return res.status(404).json({
        error: 'Domain nicht gefunden oder nicht aktiv',
        code: 'TENANT_NOT_FOUND'
      });
    }
    
    // Tenant in Request-Kontext speichern
    const tenant = result.rows[0];
    req.tenantContext = {
      isSubdomain: true,
      isMainDomain: false,
      tenant: tenant
    };
    
    // Tenant-ID für Logging und Datenisolation
    req.tenantId = tenant.id;
    
    next();
  } catch (error) {
    console.error('Fehler bei Tenant-Identifikation:', error);
    res.status(500).json({ error: 'Serverfehler bei Tenant-Identifikation' });
  }
};

module.exports = { tenantContext };
```

### 11.2 Erweiterte Kosten- und Nutzungsverfolgung
- [ ] Detailliertere Kostenberechnung implementieren:
```javascript
// services/billing.js
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Token-Preise pro 1000 Tokens (aktualisierte Liste)
const TOKEN_PRICES = {
  // Chat-Modelle
  'gpt-3.5-turbo': { prompt: 0.0015, completion: 0.002 },
  'gpt-3.5-turbo-16k': { prompt: 0.003, completion: 0.004 },
  'gpt-4': { prompt: 0.03, completion: 0.06 },
  'gpt-4-32k': { prompt: 0.06, completion: 0.12 },
  'gpt-4-turbo': { prompt: 0.01, completion: 0.03 },
  
  // Text-Completion-Modelle
  'text-davinci-003': { completion: 0.02 },
  'text-curie-001': { completion: 0.002 },
  
  // Embedding-Modelle
  'text-embedding-ada-002': { embedding: 0.0001 }
};

// Kosten berechnen basierend auf Modell und Token-Nutzung
const calculateCost = (model, promptTokens = 0, completionTokens = 0, embeddingTokens = 0) => {
  // Standardpreise falls Modell nicht gefunden
  const defaultPrices = { prompt: 0.002, completion: 0.002, embedding: 0.0001 };
  const prices = TOKEN_PRICES[model] || defaultPrices;
  
  let totalCost = 0;
  
  // Prompt-Tokens berechnen
  if (promptTokens > 0 && prices.prompt) {
    totalCost += (promptTokens / 1000) * prices.prompt;
  }
  
  // Completion-Tokens berechnen
  if (completionTokens > 0 && prices.completion) {
    totalCost += (completionTokens / 1000) * prices.completion;
  }
  
  // Embedding-Tokens berechnen
  if (embeddingTokens > 0 && prices.embedding) {
    totalCost += (embeddingTokens / 1000) * prices.embedding;
  }
  
  return totalCost;
};

// Monatliche Abrechnung generieren
const generateMonthlyInvoice = async (tenantId, month, year) => {
  try {
    const startDate = new Date(year, month - 1, 1);
    const endDate = new Date(year, month, 0);
    
    // Alle Nutzungsdaten für den Zeitraum sammeln
    const usageResult = await pool.query(
      `SELECT 
        SUM(prompt_tokens) as total_prompt_tokens,
        SUM(completion_tokens) as total_completion_tokens,
        SUM(total_tokens) as total_tokens,
        SUM(kosten) as total_cost,
        COUNT(*) as request_count
       FROM apianfragen
       WHERE kunden_id = $1 
       AND zeitstempel >= $2 
       AND zeitstempel <= $3`,
      [tenantId, startDate, endDate]
    );
    
    // Modellbasierte Aufschlüsselung
    const modelBreakdownResult = await pool.query(
      `SELECT 
        modell,
        SUM(prompt_tokens) as prompt_tokens,
        SUM(completion_tokens) as completion_tokens,
        SUM(total_tokens) as total_tokens,
        SUM(kosten) as cost,
        COUNT(*) as requests
       FROM apianfragen
       WHERE kunden_id = $1 
       AND zeitstempel >= $2 
       AND zeitstempel <= $3
       GROUP BY modell
       ORDER BY cost DESC`,
      [tenantId, startDate, endDate]
    );
    
    // Tagesbasierte Nutzung für Grafiken
    const dailyUsageResult = await pool.query(
      `SELECT 
        DATE(zeitstempel) as date,
        SUM(total_tokens) as tokens,
        SUM(kosten) as cost
       FROM apianfragen
       WHERE kunden_id = $1 
       AND zeitstempel >= $2 
       AND zeitstempel <= $3
       GROUP BY date
       ORDER BY date`,
      [tenantId, startDate, endDate]
    );
    
    // Kunde abrufen
    const customerResult = await pool.query(
      'SELECT * FROM kunden WHERE id = $1',
      [tenantId]
    );
    
    if (customerResult.rows.length === 0) {
      throw new Error(`Kunde mit ID ${tenantId} nicht gefunden`);
    }
    
    const customer = customerResult.rows[0];
    const usageSummary = usageResult.rows[0];
    
    // Prüfen, ob bereits eine Abrechnung existiert
    const existingInvoiceResult = await pool.query(
      'SELECT id FROM abrechnungen WHERE kunden_id = $1 AND monat = $2 AND jahr = $3',
      [tenantId, month, year]
    );
    
    let invoiceId;
    
    if (existingInvoiceResult.rows.length > 0) {
      // Bestehende Abrechnung aktualisieren
      invoiceId = existingInvoiceResult.rows[0].id;
      
      await pool.query(
        `UPDATE abrechnungen 
         SET gesamttokens = $1, 
             gesamtkosten = $2, 
             anfragen_anzahl = $3,
             aktualisiert_am = CURRENT_TIMESTAMP
         WHERE id = $4`,
        [
          usageSummary.total_tokens || 0,
          usageSummary.total_cost || 0,
          usageSummary.request_count || 0,
          invoiceId
        ]
      );
    } else {
      // Neue Abrechnung erstellen
      const newInvoiceResult = await pool.query(
        `INSERT INTO abrechnungen 
           (kunden_id, monat, jahr, gesamttokens, gesamtkosten, anfragen_anzahl)
         VALUES ($1, $2, $3, $4, $5, $6)
         RETURNING id`,
        [
          tenantId,
          month,
          year,
          usageSummary.total_tokens || 0,
          usageSummary.total_cost || 0,
          usageSummary.request_count || 0
        ]
      );
      
      invoiceId = newInvoiceResult.rows[0].id;
    }
    
    // Detaillierte Modellnutzung in separater Tabelle speichern
    // Zuerst alte Einträge löschen
    await pool.query(
      'DELETE FROM abrechnung_details WHERE abrechnung_id = $1',
      [invoiceId]
    );
    
    // Neue Einträge für jedes Modell hinzufügen
    for (const model of modelBreakdownResult.rows) {
      await pool.query(
        `INSERT INTO abrechnung_details 
           (abrechnung_id, modell, prompt_tokens, completion_tokens, total_tokens, kosten, anfragen)
         VALUES ($1, $2, $3, $4, $5, $6, $7)`,
        [
          invoiceId,
          model.modell,
          model.prompt_tokens || 0,
          model.completion_tokens || 0,
          model.total_tokens || 0,
          model.cost || 0,
          model.requests || 0
        ]
      );
    }
    
    return {
      invoiceId,
      customer,
      period: { month, year },
      summary: usageSummary,
      modelBreakdown: modelBreakdownResult.rows,
      dailyUsage: dailyUsageResult.rows
    };
  } catch (error) {
    console.error('Fehler bei der Abrechnungsgenerierung:', error);
    throw error;
  }
};

// Exportieren der Funktionen
module.exports = {
  calculateCost,
  generateMonthlyInvoice,
  TOKEN_PRICES
};
```

### 11.3 Rate-Limiting und Nutzungsbeschränkungen
- [ ] Rate-Limiting-Middleware implementieren:
```javascript
// middleware/rate-limit.js
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

const rateLimiter = async (req, res, next) => {
  // Nur auf API-Endpunkten anwenden
  if (!req.path.startsWith('/api/')) {
    return next();
  }
  
  // Benötigt Tenant-Kontext von vorheriger Middleware
  if (!req.tenantContext || !req.tenantContext.tenant) {
    return next();
  }
  
  const tenant = req.tenantContext.tenant;
  
  try {
    // Aktuelle Rate abrufen (Anfragen in der letzten Minute)
    const result = await pool.query(
      `SELECT COUNT(*) as request_count 
       FROM apianfragen 
       WHERE kunden_id = $1 
       AND zeitstempel > NOW() - INTERVAL '1 minute'`,
      [tenant.id]
    );
    
    const requestCount = parseInt(result.rows[0].request_count);
    const rateLimit = tenant.rate_limit_per_minute || 100; // Standardwert: 100 Anfragen/Minute
    
    // Rate-Limit in Header setzen
    res.setHeader('X-RateLimit-Limit', rateLimit);
    res.setHeader('X-RateLimit-Remaining', Math.max(0, rateLimit - requestCount));
    
    // Rate-Limit überschritten
    if (requestCount >= rateLimit) {
      return res.status(429).json({
        error: 'Rate limit exceeded',
        message: `Limit von ${rateLimit} Anfragen pro Minute überschritten. Bitte warten Sie.`,
        retryAfter: 60 // Sekunden bis zum nächsten Versuch
      });
    }
    
    // Monatliches Token-Limit prüfen
    if (tenant.max_tokens_pro_monat > 0) {
      const today = new Date();
      const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
      
      const usageResult = await pool.query(
        `SELECT SUM(total_tokens) as monthly_tokens 
         FROM apianfragen 
         WHERE kunden_id = $1 
         AND zeitstempel >= $2`,
        [tenant.id, firstDayOfMonth]
      );
      
      const monthlyTokens = parseInt(usageResult.rows[0].monthly_tokens || 0);
      
      // Token-Limit in Header setzen
      res.setHeader('X-Token-Limit', tenant.max_tokens_pro_monat);
      res.setHeader('X-Token-Used', monthlyTokens);
      res.setHeader('X-Token-Remaining', Math.max(0, tenant.max_tokens_pro_monat - monthlyTokens));
      
      // Token-Limit überschritten
      if (monthlyTokens >= tenant.max_tokens_pro_monat) {
        return res.status(403).json({
          error: 'Token limit exceeded',
          message: `Monatliches Token-Limit von ${tenant.max_tokens_pro_monat} Tokens überschritten. Bitte kontaktieren Sie den Support.`
        });
      }
    }
    
    next();
  } catch (error) {
    console.error('Fehler beim Rate-Limiting:', error);
    // Im Fehlerfall weitermachen (keine Blockierung)
    next();
  }
};

module.exports = { rateLimiter };
```

### 11.4 Erweiterte PDF-Rechnungsgenerierung
- [ ] Service zur Erstellung von PDF-Rechnungen:
```javascript
// services/invoice-generator.js
const fs = require('fs');
const path = require('path');
const PDFDocument = require('pdfkit');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Formatierungshilfsfunktionen
const formatCurrency = (amount) => {
  return new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR'
  }).format(amount);
};

const formatDate = (date) => {
  return new Intl.DateTimeFormat('de-DE', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).format(date);
};

const formatNumber = (num) => {
  return new Intl.NumberFormat('de-DE').format(num);
};

// PDF-Rechnung generieren
const generatePDFInvoice = async (invoiceId) => {
  try {
    // Abrechnungsdaten abrufen
    const invoiceResult = await pool.query(
      `SELECT 
        a.id,
        a.monat,
        a.jahr,
        a.gesamttokens,
        a.gesamtkosten,
        a.anfragen_anzahl,
        a.erstellt_am,
        a.bezahlt_am,
        a.bezahlstatus,
        k.id as kunden_id,
        k.name as kundenname,
        k.kontakt_email
       FROM abrechnungen a
       JOIN kunden k ON a.kunden_id = k.id
       WHERE a.id = $1`,
      [invoiceId]
    );
    
    if (invoiceResult.rows.length === 0) {
      throw new Error(`Abrechnung mit ID ${invoiceId} nicht gefunden`);
    }
    
    const invoice = invoiceResult.rows[0];
    
    // Detaillierte Modellnutzung abrufen
    const detailsResult = await pool.query(
      `SELECT * FROM abrechnung_details WHERE abrechnung_id = $1 ORDER BY kosten DESC`,
      [invoiceId]
    );
    
    // Rechnungsnummer generieren
    const invoiceNumber = `INV-${invoice.jahr}${invoice.monat.toString().padStart(2, '0')}-${invoice.kunden_id}`;
    
    // Datum für Rechnung
    const invoiceDate = invoice.erstellt_am || new Date();
    const dueDate = new Date(invoiceDate);
    dueDate.setDate(dueDate.getDate() + 14); // Zahlungsziel: 14 Tage
    
    // Anbieter-Informationen
    const company = {
      name: 'Ihre Firma GmbH',
      address: 'Musterstraße 123',
      city: '12345 Musterstadt',
      country: 'Deutschland',
      email: 'billing@ihrefirma.de',
      website: 'www.ihrefirma.de',
      taxId: 'USt-IdNr.: DE123456789'
    };
    
    // PDF-Dokument erstellen
    const doc = new PDFDocument({ margin: 50 });
    
    // Ausgabepfad für die PDF
    const uploadDir = path.join(__dirname, '../uploads/invoices');
    
    // Sicherstellen, dass das Verzeichnis existiert
    if (!fs.existsSync(uploadDir)) {
      fs.mkdirSync(uploadDir, { recursive: true });
    }
    
    const filePath = path.join(uploadDir, `${invoiceNumber}.pdf`);
    const writeStream = fs.createWriteStream(filePath);
    
    doc.pipe(writeStream);
    
    // Firmenlogo (falls vorhanden)
    const logoPath = path.join(__dirname, '../public/images/logo.png');
    if (fs.existsSync(logoPath)) {
      doc.image(logoPath, 50, 45, { width: 150 });
    }
    
    // Firmeninformationen
    doc.fontSize(10)
       .text(company.name, 200, 50, { align: 'right' })
       .text(company.address, 200, 65, { align: 'right' })
       .text(company.city, 200, 80, { align: 'right' })
       .text(company.country, 200, 95, { align: 'right' })
       .text(company.email, 200, 110, { align: 'right' })
       .text(company.website, 200, 125, { align: 'right' });
    
    // Rechnungstitel
    doc.fontSize(20)
       .text('Rechnung', 50, 160);
    
    // Rechnungsdetails
    doc.fontSize(10)
       .text(`Rechnungsnummer: ${invoiceNumber}`, 50, 200)
       .text(`Rechnungsdatum: ${formatDate(invoiceDate)}`, 50, 215)
       .text(`Zahlungsziel: ${formatDate(dueDate)}`, 50, 230);
    
    // Kundeninformationen
    doc.fontSize(10)
       .text('Rechnungsempfänger:', 300, 200)
       .text(invoice.kundenname, 300, 215)
       .text(`Kundennummer: ${invoice.kunden_id}`, 300, 230)
       .text(invoice.kontakt_email || '', 300, 245);
    
    // Abrechnungszeitraum
    const monthNames = [
      'Januar', 'Februar', 'März', 'April', 'Mai', 'Juni',
      'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'
    ];
    
    doc.fontSize(12)
       .text(`Abrechnung für ${monthNames[invoice.monat - 1]} ${invoice.jahr}`, 50, 280);
    
    // Tabellenkopf
    doc.fontSize(10)
       .rect(50, 310, 500, 20)
       .fill('#f6f6f6');
    
    doc.fillColor('#000000')
       .text('Beschreibung', 60, 318)
       .text('Menge', 280, 318, { width: 90, align: 'right' })
       .text('Einheitspreis', 370, 318, { width: 90, align: 'right' })
       .text('Betrag', 460, 318, { width: 80, align: 'right' });
    
    // Trennlinie
    doc.moveTo(50, 330)
       .lineTo(550, 330)
       .stroke();
    
    // Startposition für Artikel
    let y = 340;
    
    // Grundgebühr (falls vorhanden)
    if (invoice.grundgebuehr > 0) {
      doc.text('Grundgebühr', 60, y)
         .text('1', 280, y, { width: 90, align: 'right' })
         .text(formatCurrency(invoice.grundgebuehr), 370, y, { width: 90, align: 'right' })
         .text(formatCurrency(invoice.grundgebuehr), 460, y, { width: 80, align: 'right' });
      
      y += 20;
    }
    
    // Gesamtnutzung
    doc.text(`API-Nutzung (${formatNumber(invoice.gesamttokens)} Tokens)`, 60, y)
       .text('1', 280, y, { width: 90, align: 'right' })
       .text(formatCurrency(invoice.gesamtkosten), 370, y, { width: 90, align: 'right' })
       .text(formatCurrency(invoice.gesamtkosten), 460, y, { width: 80, align: 'right' });
    
    y += 20;
    
    // Aufschlüsselung nach Modellen
    if (detailsResult.rows.length > 0) {
      doc.fontSize(9)
         .text('Modellnutzung im Detail:', 60, y + 10);
      
      y += 25;
      
      for (const detail of detailsResult.rows) {
        if (y > 700) {
          // Neue Seite wenn nicht genug Platz
          doc.addPage();
          y = 50;
        }
        
        doc.text(`- ${detail.modell}: ${formatNumber(detail.total_tokens)} Tokens`, 70, y)
           .text(formatCurrency(detail.kosten), 460, y, { width: 80, align: 'right' });
        
        y += 15;
      }
    }
    
    // Trennlinie vor Gesamtsumme
    doc.moveTo(50, y)
       .lineTo(550, y)
       .stroke();
    
    // Gesamtsumme
    doc.fontSize(10)
       .text('Gesamtbetrag (netto)', 350, y + 10)
       .text(formatCurrency(invoice.gesamtkosten), 460, y + 10, { width: 80, align: 'right' });
    
    // Mehrwertsteuer (falls anwendbar)
    const taxRate = 0.19; // 19% MwSt.
    const taxAmount = invoice.gesamtkosten * taxRate;
    
    doc.text(`Mehrwertsteuer (${(taxRate * 100).toFixed(0)}%)`, 350, y + 25)
       .text(formatCurrency(taxAmount), 460, y + 25, { width: 80, align: 'right' });
    
    // Gesamtbetrag mit MwSt.
    const totalWithTax = invoice.gesamtkosten + taxAmount;
    
    doc.fontSize(12)
       .text('Gesamtbetrag (brutto)', 350, y + 45, { fontWeight: 'bold' })
       .text(formatCurrency(totalWithTax), 460, y + 45, { width: 80, align: 'right', fontWeight: 'bold' });
    
    // Zahlungsinformationen
    doc.fontSize(10)
       .text('Zahlungsinformationen', 50, y + 80)
       .text('Bitte überweisen Sie den Rechnungsbetrag unter Angabe der Rechnungsnummer bis zum', 50, y + 95)
       .text(`Zahlungsziel: ${formatDate(dueDate)}`, 50, y + 110)
       .text('Bankverbindung: Musterbank', 50, y + 125)
       .text('IBAN: DE12 3456 7890 1234 5678 90', 50, y + 140)
       .text('BIC: MUSTBANKXXX', 50, y + 155);
    
    // Fußzeile
    doc.fontSize(8)
       .text(company.name, 50, 750)
       .text(company.taxId, 50, 760)
       .text('Seite 1 von 1', 500, 760);
    
    // Dokument finalisieren
    doc.end();
    
    // Auf fertige PDF warten
    return new Promise((resolve, reject) => {
      writeStream.on('finish', () => {
        resolve({
          filepath: filePath,
          filename: `${invoiceNumber}.pdf`,
          invoiceNumber
        });
      });
      
      writeStream.on('error', reject);
    });
  } catch (error) {
    console.error('Fehler bei der PDF-Generierung:', error);
    throw error;
  }
};

module.exports = {
  generatePDFInvoice
};
```

### 11.5 API-Endpunkte erweitern
- [ ] Weitere API-Endpunkte implementieren:
```javascript
// routes/api.js (erweitert)
const express = require('express');
const router = express.Router();
const openaiService = require('../services/openai');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Haupt-Chat-Completion-Endpunkt
router.post('/completion', async (req, res) => {
  try {
    const tenant = req.tenantContext.tenant;
    const result = await openaiService.createChatCompletion(tenant.id, req.body);
    
    if (result.error) {
      return res.status(400).json({ error: result.error });
    }
    
    res.json(result.data);
  } catch (error) {
    console.error('API-Fehler:', error);
    res.status(500).json({ error: 'Server-Fehler', message: error.message });
  }
});

// Embedding-Endpunkt
router.post('/embedding', async (req, res) => {
  try {
    const tenant = req.tenantContext.tenant;
    const result = await openaiService.createEmbedding(tenant.id, req.body);
    
    if (result.error) {
      return res.status(400).json({ error: result.error });
    }
    
    res.json(result.data);
  } catch (error) {
    console.error('Embedding-API-Fehler:', error);
    res.status(500).json({ error: 'Server-Fehler', message: error.message });
  }
});

// Nutzungsstatistik-Endpunkt
router.get('/usage', async (req, res) => {
  try {
    const tenant = req.tenantContext.tenant;
    
    // Aktueller Monat
    const now = new Date();
    const currentMonth = now.getMonth() + 1;
    const currentYear = now.getFullYear();
    
    // Monatliche Nutzung abrufen
    const usageResult = await pool.query(
      `SELECT 
        SUM(total_tokens) as tokens,
        SUM(kosten) as cost
       FROM apianfragen
       WHERE kunden_id = $1
       AND EXTRACT(MONTH FROM zeitstempel) = $2
       AND EXTRACT(YEAR FROM zeitstempel) = $3`,
      [tenant.id, currentMonth, currentYear]
    );
    
    // Tägliche Nutzung für den aktuellen Monat
    const dailyUsageResult = await pool.query(
      `SELECT 
        DATE(zeitstempel) as date,
        SUM(total_tokens) as tokens,
        SUM(kosten) as cost
       FROM apianfragen
       WHERE kunden_id = $1
       AND EXTRACT(MONTH FROM zeitstempel) = $2
       AND EXTRACT(YEAR FROM zeitstempel) = $3
       GROUP BY date
       ORDER BY date`,
      [tenant.id, currentMonth, currentYear]
    );
    
    // Nutzung nach Modell
    const modelUsageResult = await pool.query(
      `SELECT 
        modell,
        SUM(total_tokens) as tokens,
        COUNT(*) as requests
       FROM apianfragen
       WHERE kunden_id = $1
       AND EXTRACT(MONTH FROM zeitstempel) = $2
       AND EXTRACT(YEAR FROM zeitstempel) = $3
       GROUP BY modell
       ORDER BY tokens DESC`,
      [tenant.id, currentMonth, currentYear]
    );
    
    res.json({
      tenant: {
        id: tenant.id,
        name: tenant.name,
        monthly_limit: tenant.max_tokens_pro_monat
      },
      current_period: {
        month: currentMonth,
        year: currentYear
      },
      monthly_usage: {
        tokens: parseInt(usageResult.rows[0]?.tokens || 0),
        cost: parseFloat(usageResult.rows[0]?.cost || 0)
      },
      daily_usage: dailyUsageResult.rows,
      model_usage: modelUsageResult.rows
    });
  } catch (error) {
    console.error('Nutzungsstatistik-Fehler:', error);
    res.status(500).json({ error: 'Server-Fehler', message: error.message });
  }
});

module.exports = router;
```

## 12. Sicherheitsverbesserungen und Best Practices

### 5.1 CSS für Admin-Dashboard
- [ ] Basis-Styling für das Admin-Dashboard erstellen:
```css
/* public/css/styles.css */
:root {
  --primary: #4361ee;
  --secondary: #3f37c9;
  --success: #4cc9f0;
  --danger: #f72585;
  --warning: #f8961e;
  --light: #f8f9fa;
  --dark: #212529;
  --gray: #6c757d;
  --border: #dee2e6;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  line-height: 1.6;
  color: var(--dark);
  background-color: #f5f7fb;
}

a {
  color: var(--primary);
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

/* Layout */
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background-color: var(--dark);
  color: white;
  padding: 1rem;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
}

.content {
  flex: 1;
  margin-left: 250px;
  padding: 2rem;
}

/* Sidebar */
.sidebar-header {
  padding-bottom: 1rem;
  margin-bottom: 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header h1 {
  font-size: 1.5rem;
}

.sidebar-nav ul {
  list-style: none;
}

.sidebar-nav li {
  margin-bottom: 0.5rem;
}

.sidebar-nav a {
  color: rgba(255, 255, 255, 0.8);
  display: block;
  padding: 0.5rem;
  border-radius: 4px;
  transition: all 0.2s;
}

.sidebar-nav a:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
  text-decoration: none;
}

/* Cards */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.card {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.card h3 {
  font-size: 0.875rem;
  color: var(--gray);
  margin-bottom: 0.5rem;
}

.card .number {
  font-size: 2rem;
  font-weight: bold;
}

/* Tables */
.data-tables {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem;
  margin-top: 2rem;
}

@media (min-width: 1024px) {
  .data-tables {
    grid-template-columns: 1fr 1fr;
  }
}

.table-container {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid var(--border);
}

.data-table th {
  font-weight: 600;
  color: var(--gray);
}

/* Status indicators */
.status-erfolg {
  color: var(--success);
}

.status-fehler {
  color: var(--danger);
}

.status-aktiv {
  color: var(--success);
}

.status-inaktiv {
  color: var(--danger);
}

.status-offen {
  color: var(--warning);
}

.status-bezahlt {
  color: var(--success);
}

/* Forms */
.form-container {
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border);
  border-radius: 4px;
  font-size: 1rem;
}

.form-control:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.1);
}

.btn {
  display: inline-block;
  padding: 0.5rem 1rem;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.2s;
}

.btn:hover {
  background-color: var(--secondary);
  text-decoration: none;
}

.btn-danger {
  background-color: var(--danger);
}

.btn-danger:hover {
  background-color: #d01568;
}

.btn-success {
  background-color: var(--success);
}

.btn-success:hover {
  background-color: #3ab8df;
}

/* Charts */
.chart-container {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  margin-bottom: 2rem;
}
```

### 5.2 Javascript für Admin-Dashboard
- [ ] Client-seitiges Javascript für interaktive Elemente:
```javascript
// public/js/admin.js
document.addEventListener('DOMContentLoaded', function() {
  // Aktiver Navigation-Link hervorheben
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll('.sidebar-nav a');
  
  navLinks.forEach(link => {
    if (currentPath.includes(link.getAttribute('href'))) {
      link.style.backgroundColor = 'rgba(255, 255, 255, 0.2)';
      link.style.color = 'white';
    }
  });
  
  // Toggle für mobile Sidebar
  const sidebarToggle = document.querySelector('.sidebar-toggle');
  if (sidebarToggle) {
    sidebarToggle.addEventListener('click', function() {
      document.querySelector('.sidebar').classList.toggle('active');
    });
  }
  
  // Confirm-Dialog für kritische Aktionen
  const confirmBtns = document.querySelectorAll('[data-confirm]');
  confirmBtns.forEach(btn => {
    btn.addEventListener('click', function(e) {
      if (!confirm(this.dataset.confirm)) {
        e.preventDefault();
      }
    });
  });
  
  // API-Key anzeigen/verbergen Toggle
  const apiKeyToggles = document.querySelectorAll('.api-key-toggle');
  apiKeyToggles.forEach(toggle => {
    toggle.addEventListener('click', function() {
      const keyElement = document.querySelector(this.dataset.target);
      if (keyElement.type === 'password') {
        keyElement.type = 'text';
        this.textContent = 'Verbergen';
      } else {
        keyElement.type = 'password';
        this.textContent = 'Anzeigen';
      }
    });
  });
});
```

## 6. App Integration und Inbetriebnahme

### 6.1 Express App zusammenfügen
- [ ] Hauptanwendung konfigurieren:
```javascript
// app.js
const express = require('express');
const path = require('path');
const session = require('express-session');
const pgSession = require('connect-pg-simple')(session);
const { Pool } = require('pg');

// Datenbank-Verbindung
const pool = new Pool({
  connectionString: process.env.DATABASE_URL,
  ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

// Routen importieren
const apiRoutes = require('./routes/api');
const adminRoutes = require('./routes/admin');
const authRoutes = require('./routes/auth');

// Middleware importieren
const { identifyCustomerBySubdomain } = require('./middleware/customer');
const { validateApiKey } = require('./middleware/auth');

const app = express();

// View Engine Setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

// Session-Middleware
app.use(session({
  store: new pgSession({
    pool,
    tableName: 'user_sessions'
  }),
  secret: process.env.SESSION_SECRET || 'dev-secret-key',
  resave: false,
  saveUninitialized: false,
  cookie: { 
    maxAge: 24 * 60 * 60 * 1000, // 24 Stunden
    secure: process.env.NODE_ENV === 'production'
  }
}));

// Dynamische Middleware basierend auf Subdomain
app.use(identifyCustomerBySubdomain);
app.use(validateApiKey);

// Routing
app.use('/api', apiRoutes);
app.use('/admin', authRoutes);
app.use('/admin', adminRoutes);

// Hauptseite (kann später durch Landing Page ersetzt werden)
app.get('/', (req, res) => {
  if (req.isCustomerSubdomain) {
    res.json({ message: `API-Endpunkt für ${req.customer.name}. Bitte verwenden Sie /api/completion für Anfragen.` });
  } else {
    res.redirect('/admin/login');
  }
});

// 404 Handler
app.use((req, res, next) => {
  res.status(404).send('Nicht gefunden');
});

// Fehler-Handler
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send('Serverfehler');
});

module.exports = app;
```

### 6.2 Server-Einstiegspunkt
- [ ] Server-Konfiguration:
```javascript
// server.js
require('dotenv').config();

const app = require('./app');
const port = process.env.PORT || 3000;

app.listen(port, () => {
  console.log(`Server läuft auf Port ${port}`);
});
```

### 6.3 Umgebungsvariablen
- [ ] .env-Datei für lokale Entwicklung:
```
# .env Beispiel (NICHT im Produktionssystem verwenden!)
DATABASE_URL=postgres://username:password@localhost:5432/pim_ai_connector
OPENAI_API_KEY=sk-your-openai-api-key
SESSION_SECRET=super-secure-session-secret
ADMIN_USER=admin
ADMIN_PASSWORD=initial-password
PORT=3000
```

- [ ] .env.example für Dokumentation:
```
# .env.example - Beispiel-Umgebungsvariablen
DATABASE_URL=postgres://username:password@localhost:5432/pim_ai_connector
OPENAI_API_KEY=your-openai-api-key
SESSION_SECRET=your-session-secret
ADMIN_USER=admin
ADMIN_PASSWORD=secure-password
PORT=3000
```

### 6.4 Datenbank-Migration
- [ ] Datenbank-Migrationsskript:
```javascript
// scripts/db-migrate.js
require('dotenv').config();
const { Pool } = require('pg');
const bcrypt = require('bcrypt');
const fs = require('fs');
const path = require('path');

const pool = new Pool({
  connectionString: process.env.DATABASE_URL,
  ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

async function runMigration() {
  try {
    console.log('Starte Datenbank-Migration...');
    
    // SQL-Schema aus Datei lesen
    const schemaPath = path.join(__dirname, 'schema.sql');
    const schema = fs.readFileSync(schemaPath, 'utf8');
    
    // Schema ausführen
    await pool.query(schema);
    
    // Admin-Benutzer anlegen
    if (process.env.ADMIN_USER && process.env.ADMIN_PASSWORD) {
      const passwordHash = await bcrypt.hash(process.env.ADMIN_PASSWORD, 10);
      
      // Prüfen, ob Admin bereits existiert
      const result = await pool.query(
        'SELECT * FROM admin_benutzer WHERE benutzername = $1',
        [process.env.ADMIN_USER]
      );
      
      if (result.rows.length === 0) {
        // Admin anlegen
        await pool.query(
          'INSERT INTO admin_benutzer (benutzername, passwort_hash, name) VALUES ($1, $2, $3)',
          [process.env.ADMIN_USER, passwordHash, 'Administrator']
        );
        console.log(`Admin-Benutzer "${process.env.ADMIN_USER}" wurde angelegt.`);
      } else {
        console.log(`Admin-Benutzer "${process.env.ADMIN_USER}" existiert bereits.`);
      }
    }
    
    console.log('Datenbank-Migration erfolgreich abgeschlossen.');
  } catch (error) {
    console.error('Fehler bei der Datenbank-Migration:', error);
  } finally {
    await pool.end();
  }
}

runMigration();
```

- [ ] SQL-Schema in separater Datei:
```sql
-- scripts/schema.sql
-- Prüfen, ob Tabellen existieren
DO $
BEGIN
    -- Sessions-Tabelle für connect-pg-simple
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'user_sessions') THEN
        CREATE TABLE user_sessions (
            sid VARCHAR NOT NULL PRIMARY KEY,
            sess JSON NOT NULL,
            expire TIMESTAMP(6) NOT NULL
        );
        CREATE INDEX idx_user_sessions_expire ON user_sessions (expire);
    END IF;

    -- Kundentabelle
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'kunden') THEN
        CREATE TABLE kunden (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            subdomain VARCHAR(100) NOT NULL UNIQUE,
            api_key VARCHAR(255) NOT NULL UNIQUE,
            status VARCHAR(50) DEFAULT 'aktiv',
            max_tokens_pro_monat INTEGER DEFAULT 0,
            erstellungsdatum TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            kontakt_email VARCHAR(255)
        );
    END IF;

    -- API-Anfragen-Tabelle
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'apianfragen') THEN
        CREATE TABLE apianfragen (
            id SERIAL PRIMARY KEY,
            kunden_id INTEGER REFERENCES kunden(id),
            zeitstempel TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            anfrage_typ VARCHAR(100) NOT NULL,
            prompt_tokens INTEGER DEFAULT 0,
            completion_tokens INTEGER DEFAULT 0,
            total_tokens INTEGER DEFAULT 0,
            kosten DECIMAL(10, 6) DEFAULT 0,
            status VARCHAR(50) DEFAULT 'erfolg',
            modell VARCHAR(100),
            fehler TEXT
        );
        CREATE INDEX idx_apianfragen_kunden ON apianfragen (kunden_id);
        CREATE INDEX idx_apianfragen_zeitstempel ON apianfragen (zeitstempel);
    END IF;

    -- Abrechnungen-Tabelle
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'abrechnungen') THEN
        CREATE TABLE abrechnungen (
            id SERIAL PRIMARY KEY,
            kunden_id INTEGER REFERENCES kunden(id),
            monat INTEGER NOT NULL,
            jahr INTEGER NOT NULL,
            gesamttokens INTEGER DEFAULT 0,
            gesamtkosten DECIMAL(10, 2) DEFAULT 0,
            bezahlstatus VARCHAR(50) DEFAULT 'offen',
            erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            bezahlt_am TIMESTAMP,
            UNIQUE(kunden_id, monat, jahr)
        );
        CREATE INDEX idx_abrechnungen_kunde_datum ON abrechnungen (kunden_id, monat, jahr);
    END IF;

    -- Admin-Benutzer-Tabelle
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'admin_benutzer') THEN
        CREATE TABLE admin_benutzer (
            id SERIAL PRIMARY KEY,
            benutzername VARCHAR(100) NOT NULL UNIQUE,
            passwort_hash VARCHAR(255) NOT NULL,
            name VARCHAR(255),
            erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            letzter_login TIMESTAMP
        );
    END IF;
END $;
```

### 6.5 Deployment auf Render.com
- [ ] package.json für Render.com anpassen:
```json
{
  "name": "pim-ai-connector-lite-extended",
  "version": "1.0.0",
  "description": "PIM AI Connector mit Kundenverwaltung",
  "main": "server.js",
  "scripts": {
    "start": "node server.js",
    "dev": "nodemon server.js",
    "migrate": "node scripts/db-migrate.js"
  },
  "engines": {
    "node": ">=16.0.0"
  },
  "dependencies": {
    "bcrypt": "^5.1.0",
    "connect-pg-simple": "^8.0.0",
    "dotenv": "^16.0.3",
    "ejs": "^3.1.9",
    "express": "^4.18.2",
    "express-session": "^1.17.3",
    "openai": "^3.2.1",
    "pg": "^8.10.0"
  },
  "devDependencies": {
    "nodemon": "^2.0.22"
  }
}
```

- [ ] Render.com Einstellungen konfigurieren:
  - Web Service einrichten
    - Name: "pim-ai-connector"
    - Region: Frankfurt (closest to DE)
    - Branch: main
    - Build Command: `npm install && npm run migrate`
    - Start Command: `npm start`
    - Instance Type: Starter (Skalieren nach Bedarf)

- [ ] Umgebungsvariablen in Render.com setzen:
  - DATABASE_URL (wird automatisch eingerichtet, wenn PostgreSQL auf Render.com konfiguriert ist)
  - OPENAI_API_KEY
  - SESSION_SECRET
  - ADMIN_USER
  - ADMIN_PASSWORD
  - NODE_ENV=production

## 7. Kundenaufnahme und Wartung

### 7.1 Prozess für Kundenaufnahme
- [ ] Dokumentieren Sie den Prozess zur Aufnahme neuer Kunden:
  1. Admin-Dashboard öffnen und zu "Kunden" > "Neuer Kunde" navigieren
  2. Kundendaten eintragen (Name, E-Mail, gewünschte Subdomain)
  3. Optional: Token-Limit festlegen
  4. Kunden anlegen - System generiert automatisch API-Key
  5. API-Key und Endpunkt-Details an Kunden übermitteln

### 7.2 Anleitung für Kunden
- [ ] Erstellen Sie eine Kurzanleitung für Kunden:
```markdown
# PIM AI Connector - Kurzanleitung

Herzlich willkommen bei unserem PIM AI Connector Service! 
Diese Anleitung hilft Ihnen beim Einstieg.

## Zugriffsdaten

- **Ihre Subdomain**: [SUBDOMAIN].ihredomain.com
- **Ihr API-Key**: [API-KEY]

Bitte bewahren Sie Ihren API-Key sicher auf und teilen Sie ihn nicht mit unbefugten Personen.

## API-Endpunkte

### Chat Completion

**Endpunkt**: `https://[SUBDOMAIN].ihredomain.com/api/completion`

**Methode**: POST

**Header**:
```
Content-Type: application/json
X-API-Key: [API-KEY]
```

**Request-Body**:
```json
{
  "model": "gpt-3.5-turbo",
  "messages": [
    {"role": "system", "content": "Du bist ein hilfreicher Assistent."},
    {"role": "user", "content": "Hallo, wie geht es dir?"}
  ]
}
```

**Response-Beispiel**:
```json
{
  "id": "chatcmpl-123",
  "object": "chat.completion",
  "created": 1677858242,
  "model": "gpt-3.5-turbo-0613",
  "usage": {
    "prompt_tokens": 18,
    "completion_tokens": 33,
    "total_tokens": 51
  },
  "choices": [
    {
      "message": {
        "role": "assistant",
        "content": "Hallo! Als KI habe ich keine Gefühle, aber ich bin bereit, dir zu helfen. Wie kann ich dir heute behilflich sein?"
      },
      "finish_reason": "stop",
      "index": 0
    }
  ]
}
```

## Fehlerbehandlung

Häufige Fehlercodes:
- **401**: Ungültiger API-Key
- **400**: Fehlerhafte Anfrage (z.B. ungültiges JSON)
- **429**: Anfragelimit überschritten

## Abrechnung

Die Abrechnung erfolgt monatlich basierend auf Ihrem tatsächlichen Token-Verbrauch.
Sie können Ihren aktuellen Verbrauch jederzeit bei Ihrem Ansprechpartner erfragen.

## Support

Bei Fragen oder Problemen wenden Sie sich bitte an:
[SUPPORT-EMAIL]
```

### 7.3 Wartungsplan
- [ ] Regelmäßige Wartungsaufgaben planen:
  - Wöchentlich: Überprüfung der Logs auf Fehler
  - Monatlich: Datenbank-Backup
  - Monatlich: Abrechnungen generieren und an Kunden senden
  - Quartalsweise: Überprüfung der OpenAI-Preise und ggf. Anpassung der Berechnungslogik

### 7.4 Skalierungsplan
- [ ] Plan für zukünftige Skalierung:
  - Bei > 10 Kunden: Upgrade auf größeren Render.com-Plan
  - Bei > 50 Kunden: Trennung von API-Service und Admin-Dashboard
  - Bei > 100 Kunden: Einführung einer Cache-Schicht für häufige Anfragen
  - Bei > 200 Kunden: Migration zu einer Kubernetes-basierten Lösung

## 8. Sicherheitsmaßnahmen

### 8.1 Datenschutz
- [ ] Implementieren Sie Datenschutzmaßnahmen:
  - Keine Speicherung von Anfrageinhalten, nur Metadaten (Token-Anzahl, Modell)
  - Regelmäßige Löschung alter Logs (> 90 Tage)
  - Verschlüsselung der API-Keys in der Datenbank

### 8.2 API-Sicherheit
- [ ] Sicherheitsmaßnahmen für die API implementieren:
  - Rate-Limiting pro Kunde (max. 100 Anfragen pro Minute)
  - CORS-Konfiguration für Subdomain-spezifische Origins
  - IP-Logging bei fehlgeschlagenen Authentifizierungsversuchen

### 8.3 Admin-Sicherheit
- [ ] Sicherheitsmaßnahmen für das Admin-Dashboard:
  - 2FA für Admin-Zugänge (optional)
  - Automatische Abmeldung nach 30 Minuten Inaktivität
  - Beschränkter Zugriff basierend auf IP-Adressbereichen

## 9. Zukunftserweiterungen

### 9.1 Potenzielle Erweiterungen
- [ ] Mögliche zukünftige Funktionen:
  - Nutzerfreundliches Frontend für Kunden zur Selbstverwaltung
  - Erweiterte Analysen und Reporting-Funktionen
  - Integration weiterer KI-Modelle (z.B. Anthropic Claude, Mistral)
  - API-Endpunkte für verschiedene Aufgaben (Bilderzeugung, Embedding)
  - Eigenes Speichersystem für Konversationshistorie pro Kunde

## 10. Projektabschluss

### 10.1 Abnahmekriterien
- [ ] Checkliste für die erfolgreiche Implementierung:
  - Admin-Dashboard funktionsfähig
  - Kunde kann über Subdomain API-Anfragen stellen
  - Nutzungsprotokollierung funktioniert korrekt
  - Abrechnungssystem erstellt korrekte Abrechnungen
  - Dokumentation ist vollständig

### 10.2 Testszenario
- [ ] Testprozess für die Abnahme:
  1. Testkunden anlegen und API-Key generieren
  2. API-Anfragen mit gültigem und ungültigem API-Key testen
  3. Admin-Dashboard-Funktionen testen (Kunde anlegen, bearbeiten, Statistiken anzeigen)
  4. Abrechnungsprozess testen
  5. Sicherheitstests durchführen (CORS, Rate-Limiting, Auth)ragen 
       WHERE kunden_id = $1 
       AND EXTRACT(MONTH FROM zeitstempel) = $2
       AND EXTRACT(YEAR FROM zeitstempel) = $3`,
      [customerId, currentMonth, currentYear]
    );
    
    const monthlyUsage = parseInt(usageResult.rows[0].monthly_usage || 0);
    
    // Prüfen, ob Limit überschritten
    if (monthlyUsage >= customer.max_tokens_pro_monat) {
      return { 
        allowed: false, 
        message: `Monatliches Token-Limit (${customer.max_tokens_pro_monat}) überschritten` 
      };
    }
    
    return { allowed: true };
  } catch (error) {
    console.error('Fehler bei der Limitprüfung:', error);
    // Im Fehlerfall erlauben (damit der Service nicht steht)
    return { allowed: true };
  }
};

module.exports = {
  createChatCompletion,
  calculateCost,
  TOKEN_PRICES
};
```

### 3.4 API-Routen implementieren
- [ ] API-Routen für Kunden-Subdomains:
```javascript
// routes/api.js
const express = require('express');
const router = express.Router();
const openaiService = require('../services/openai');

// Chat Completion Endpunkt
router.post('/completion', async (req, res) => {
  try {
    // Kundenobjekt aus Middleware
    const customer = req.customer;
    
    if (!customer) {
      return res.status(400).json({ error: 'Kundenkontext fehlt' });
    }
    
    // OpenAI-Anfrage weiterleiten
    const result = await openaiService.createChatCompletion(customer.id, req.body);
    
    if (result.error) {
      return res.status(400).json({ error: result.error });
    }
    
    // Erfolgreiche Antwort
    res.json(result.data);
  } catch (error) {
    console.error('API-Fehler:', error);
    res.status(500).json({ error: 'Server-Fehler', message: error.message });
  }
});

module.exports = router;
```

## 4. Admin-Dashboard Implementierung

### 4.1 Authentifizierung für Admin-Dashboard
- [ ] Session-Middleware einrichten:
```javascript
// app.js (Teilauszug)
const session = require('express-session');
const pgSession = require('connect-pg-simple')(session);
const pool = require('./config/database');

// Session-Middleware
app.use(session({
  store: new pgSession({
    pool,
    tableName: 'user_sessions'   // Verwende eine eigene Tabelle für Sessions
  }),
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false,
  cookie: { 
    maxAge: 24 * 60 * 60 * 1000, // 24 Stunden
    secure: process.env.NODE_ENV === 'production'
  }
}));
```

- [ ] Login-Routes für Admin erstellen:
```javascript
// routes/auth.js
const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Login-Seite anzeigen
router.get('/login', (req, res) => {
  res.render('auth/login', { title: 'Admin Login', error: null });
});

// Login-Formular verarbeiten
router.post('/login', async (req, res) => {
  try {
    const { username, password } = req.body;
    
    // Admin-Benutzer in DB suchen
    const result = await pool.query(
      'SELECT * FROM admin_benutzer WHERE benutzername = $1',
      [username]
    );
    
    if (result.rows.length === 0) {
      return res.render('auth/login', { 
        title: 'Admin Login', 
        error: 'Ungültige Anmeldedaten' 
      });
    }
    
    const admin = result.rows[0];
    
    // Passwort überprüfen
    const passwordValid = await bcrypt.compare(password, admin.passwort_hash);
    
    if (!passwordValid) {
      return res.render('auth/login', { 
        title: 'Admin Login', 
        error: 'Ungültige Anmeldedaten' 
      });
    }
    
    // Login erfolgreich - Session setzen
    req.session.isAdmin = true;
    req.session.adminId = admin.id;
    req.session.adminName = admin.name;
    
    // Letzten Login aktualisieren
    await pool.query(
      'UPDATE admin_benutzer SET letzter_login = CURRENT_TIMESTAMP WHERE id = $1',
      [admin.id]
    );
    
    // Zum Dashboard weiterleiten
    res.redirect('/admin/dashboard');
  } catch (error) {
    console.error('Login-Fehler:', error);
    res.render('auth/login', { 
      title: 'Admin Login', 
      error: 'Server-Fehler. Bitte versuchen Sie es später erneut.' 
    });
  }
});

// Logout
router.get('/logout', (req, res) => {
  req.session.destroy(err => {
    if (err) {
      console.error('Logout-Fehler:', err);
    }
    res.redirect('/admin/login');
  });
});

module.exports = router;
```

### 4.2 Admin-Dashboard Views (EJS)
- [ ] EJS als Template-Engine einrichten
- [ ] Haupt-Layout-Templates erstellen:
```ejs
<!-- views/partials/header.ejs -->
<!DOCTYPE html>
<html lang="de">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= title %> - PIM AI Connector</title>
  <link rel="stylesheet" href="/css/styles.css">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
  <div class="admin-layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h1>PIM AI Admin</h1>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li><a href="/admin/dashboard">Dashboard</a></li>
          <li><a href="/admin/kunden">Kunden</a></li>
          <li><a href="/admin/abrechnung">Abrechnung</a></li>
          <li><a href="/admin/einstellungen">Einstellungen</a></li>
          <li><a href="/admin/logout">Abmelden</a></li>
        </ul>
      </nav>
    </aside>
    <main class="content">
```

```ejs
<!-- views/partials/footer.ejs -->
    </main>
  </div>
  <script src="/js/admin.js"></script>
</body>
</html>
```

- [ ] Dashboard Hauptseite:
```ejs
<!-- views/admin/dashboard.ejs -->
<%- include('../partials/header') %>

<div class="dashboard">
  <h1>Dashboard</h1>
  
  <div class="stats-cards">
    <div class="card">
      <h3>Kunden</h3>
      <div class="number"><%= statistiken.kundenAnzahl %></div>
    </div>
    <div class="card">
      <h3>Tokens heute</h3>
      <div class="number"><%= statistiken.tokensHeute.toLocaleString() %></div>
    </div>
    <div class="card">
      <h3>Tokens Monat</h3>
      <div class="number"><%= statistiken.tokensMonat.toLocaleString() %></div>
    </div>
    <div class="card">
      <h3>Umsatz Monat</h3>
      <div class="number">€<%= statistiken.umsatzMonat.toFixed(2) %></div>
    </div>
  </div>
  
  <div class="chart-container">
    <h2>Token-Verbrauch (letzte 30 Tage)</h2>
    <canvas id="usageChart"></canvas>
  </div>
  
  <div class="data-tables">
    <div class="table-container">
      <h2>Top Kunden (nach Verbrauch)</h2>
      <table class="data-table">
        <thead>
          <tr>
            <th>Kunde</th>
            <th>Tokens (30 Tage)</th>
            <th>Kosten</th>
          </tr>
        </thead>
        <tbody>
          <% topKunden.forEach(kunde => { %>
          <tr>
            <td><a href="/admin/kunden/<%= kunde.id %>"><%= kunde.name %></a></td>
            <td><%= kunde.tokens.toLocaleString() %></td>
            <td>€<%= kunde.kosten.toFixed(2) %></td>
          </tr>
          <% }); %>
        </tbody>
      </table>
    </div>
    
    <div class="table-container">
      <h2>Letzte Anfragen</h2>
      <table class="data-table">
        <thead>
          <tr>
            <th>Zeitpunkt</th>
            <th>Kunde</th>
            <th>Modell</th>
            <th>Tokens</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <% letzteAnfragen.forEach(anfrage => { %>
          <tr>
            <td><%= new Date(anfrage.zeitstempel).toLocaleString() %></td>
            <td><%= anfrage.kundenname %></td>
            <td><%= anfrage.modell %></td>
            <td><%= anfrage.total_tokens %></td>
            <td><span class="status-<%= anfrage.status %>"><%= anfrage.status %></span></td>
          </tr>
          <% }); %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
  // Charts initialisieren
  const ctx = document.getElementById('usageChart').getContext('2d');
  
  const usageChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: <%- JSON.stringify(chartDaten.labels) %>,
      datasets: [{
        label: 'Token-Verbrauch',
        data: <%- JSON.stringify(chartDaten.tokens) %>,
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
</script>

<%- include('../partials/footer') %>
```

### 4.3 Admin-Dashboard Routes
- [ ] Dashboard-Routen:
```javascript
// routes/admin.js
const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });
const bcrypt = require('bcrypt');
const crypto = require('crypto');

// Admin-Middleware
const isAdmin = (req, res, next) => {
  if (req.session.isAdmin) {
    next();
  } else {
    res.redirect('/admin/login');
  }
};

// Dashboard Hauptseite
router.get('/dashboard', isAdmin, async (req, res) => {
  try {
    // Datum für Abfragen
    const heute = new Date();
    const ersterTagDesMonats = new Date(heute.getFullYear(), heute.getMonth(), 1);
    
    // Basis-Statistiken
    const [kundenResult, tokensHeuteResult, tokensMonatResult] = await Promise.all([
      pool.query('SELECT COUNT(*) as anzahl FROM kunden'),
      pool.query(
        `SELECT SUM(total_tokens) as tokens, SUM(kosten) as kosten 
         FROM apianfragen 
         WHERE DATE(zeitstempel) = CURRENT_DATE`
      ),
      pool.query(
        `SELECT SUM(total_tokens) as tokens, SUM(kosten) as kosten 
         FROM apianfragen 
         WHERE zeitstempel >= $1`,
        [ersterTagDesMonats]
      )
    ]);
    
    // Top-Kunden
    const topKundenResult = await pool.query(
      `SELECT k.id, k.name, SUM(a.total_tokens) as tokens, SUM(a.kosten) as kosten
       FROM kunden k
       JOIN apianfragen a ON k.id = a.kunden_id
       WHERE a.zeitstempel >= CURRENT_DATE - INTERVAL '30 days'
       GROUP BY k.id, k.name
       ORDER BY tokens DESC
       LIMIT 5`
    );
    
    // Letzte Anfragen
    const letzteAnfragenResult = await pool.query(
      `SELECT a.zeitstempel, a.modell, a.total_tokens, a.status, k.name as kundenname
       FROM apianfragen a
       JOIN kunden k ON a.kunden_id = k.id
       ORDER BY a.zeitstempel DESC
       LIMIT 10`
    );
    
    // Chart-Daten (letzte 30 Tage)
    const chartResult = await pool.query(
      `SELECT DATE(zeitstempel) as tag, SUM(total_tokens) as tokens
       FROM apianfragen
       WHERE zeitstempel >= CURRENT_DATE - INTERVAL '30 days'
       GROUP BY tag
       ORDER BY tag`
    );
    
    // Chart-Daten formatieren
    const labels = [];
    const tokenData = [];
    
    chartResult.rows.forEach(row => {
      labels.push(new Date(row.tag).toLocaleDateString());
      tokenData.push(row.tokens);
    });
    
    // Daten für Template zusammenstellen
    const statistiken = {
      kundenAnzahl: parseInt(kundenResult.rows[0].anzahl),
      tokensHeute: parseInt(tokensHeuteResult.rows[0].tokens || 0),
      tokensMonat: parseInt(tokensMonatResult.rows[0].tokens || 0),
      umsatzMonat: parseFloat(tokensMonatResult.rows[0].kosten || 0)
    };
    
    res.render('admin/dashboard', {
      title: 'Admin Dashboard',
      statistiken,
      topKunden: topKundenResult.rows,
      letzteAnfragen: letzteAnfragenResult.rows,
      chartDaten: {
        labels,
        tokens: tokenData
      }
    });
  } catch (error) {
    console.error('Dashboard-Fehler:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Kundenliste
router.get('/kunden', isAdmin, async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM kunden ORDER BY name');
    res.render('admin/kunden/liste', {
      title: 'Kundenliste',
      kunden: result.rows
    });
  } catch (error) {
    console.error('Kundenlisten-Fehler:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Kundendetails
router.get('/kunden/:id', isAdmin, async (req, res) => {
  try {
    const { id } = req.params;
    
    // Kundeninformationen
    const kundeResult = await pool.query(
      'SELECT * FROM kunden WHERE id = $1',
      [id]
    );
    
    if (kundeResult.rows.length === 0) {
      return res.status(404).send('Kunde nicht gefunden');
    }
    
    const kunde = kundeResult.rows[0];
    
    // Nutzungsstatistik (letzte 30 Tage)
    const nutzungResult = await pool.query(
      `SELECT 
        DATE(zeitstempel) as tag,
        SUM(total_tokens) as tokens,
        SUM(kosten) as kosten,
        COUNT(*) as anfragen
       FROM apianf