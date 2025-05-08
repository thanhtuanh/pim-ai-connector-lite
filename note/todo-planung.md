# Admin-Dashboard für Ihr System

Für ein kosteneffizientes Admin-Dashboard gibt es mehrere gute Optionen. Hier ist meine Empfehlung für die beste Lösung:

## Option 1: Direkt in die bestehende Node.js-Anwendung integrieren

### Vorteile:
- Keine zusätzliche Infrastruktur nötig
- Direkter Zugriff auf die Datenbank
- Einfache Wartung (alles in einer Anwendung)

### Technologie-Stack:
- **Backend**: Ihre bestehende Node.js-Anwendung
- **Frontend**: Express + EJS/Handlebars oder React/Vue eingebunden
- **Authentifizierung**: Passport.js oder JWT
- **Visualisierung**: Chart.js für Diagramme und Statistiken

### Implementierung:

```javascript
// Admin-Routen in Express
const express = require('express');
const router = express.Router();
const { Pool } = require('pg');
const pool = new Pool({ connectionString: process.env.DATABASE_URL });

// Admin-Authentifizierung Middleware
const isAdmin = (req, res, next) => {
  if (req.session.isAdmin) {
    next();
  } else {
    res.redirect('/admin/login');
  }
};

// Login-Route
router.get('/admin/login', (req, res) => {
  res.render('admin/login');
});

router.post('/admin/login', (req, res) => {
  // Sichere Authentifizierung implementieren
  if (req.body.username === process.env.ADMIN_USER && 
      req.body.password === process.env.ADMIN_PASSWORD) {
    req.session.isAdmin = true;
    res.redirect('/admin/dashboard');
  } else {
    res.render('admin/login', { error: 'Ungültige Anmeldedaten' });
  }
});

// Dashboard-Hauptseite
router.get('/admin/dashboard', isAdmin, async (req, res) => {
  try {
    // Kundenliste abrufen
    const kunden = await pool.query('SELECT * FROM kunden ORDER BY name');
    
    // Gesamtnutzung (letzte 30 Tage)
    const nutzung = await pool.query(`
      SELECT SUM(total_tokens) as total_tokens, 
             SUM(kosten) as total_kosten 
      FROM apianfragen 
      WHERE zeitstempel > NOW() - INTERVAL '30 days'
    `);
    
    res.render('admin/dashboard', {
      kunden: kunden.rows,
      nutzung: nutzung.rows[0],
      title: 'Admin Dashboard'
    });
  } catch (error) {
    console.error('Dashboard-Fehler:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Kundendetails anzeigen
router.get('/admin/kunden/:id', isAdmin, async (req, res) => {
  try {
    const { id } = req.params;
    
    // Kundeninformationen
    const kunde = await pool.query('SELECT * FROM kunden WHERE id = $1', [id]);
    
    // Nutzungsstatistiken (letzte 30 Tage)
    const statistik = await pool.query(`
      SELECT 
        DATE_TRUNC('day', zeitstempel) as tag,
        SUM(total_tokens) as tokens,
        SUM(kosten) as kosten,
        COUNT(*) as anfragen
      FROM apianfragen 
      WHERE kunden_id = $1 AND zeitstempel > NOW() - INTERVAL '30 days'
      GROUP BY tag
      ORDER BY tag
    `, [id]);
    
    res.render('admin/kundendetails', {
      kunde: kunde.rows[0],
      statistik: statistik.rows,
      title: `Kunde: ${kunde.rows[0].name}`
    });
  } catch (error) {
    console.error('Kundendetails-Fehler:', error);
    res.status(500).send('Server-Fehler');
  }
});

// Export des Routers
module.exports = router;
```

### Views (EJS-Beispiel):

```ejs
<!-- dashboard.ejs -->
<%- include('partials/header') %>

<div class="container">
  <h1>Admin Dashboard</h1>
  
  <div class="stats-cards">
    <div class="card">
      <h3>Kunden</h3>
      <div class="number"><%= kunden.length %></div>
    </div>
    <div class="card">
      <h3>Tokens (30 Tage)</h3>
      <div class="number"><%= nutzung.total_tokens.toLocaleString() %></div>
    </div>
    <div class="card">
      <h3>Kosten (30 Tage)</h3>
      <div class="number">€<%= nutzung.total_kosten.toFixed(2) %></div>
    </div>
  </div>
  
  <h2>Kundenliste</h2>
  <table class="data-table">
    <thead>
      <tr>
        <th>Name</th>
        <th>Subdomain</th>
        <th>Status</th>
        <th>Erstellt am</th>
        <th>Aktionen</th>
      </tr>
    </thead>
    <tbody>
      <% kunden.forEach(kunde => { %>
      <tr>
        <td><%= kunde.name %></td>
        <td><%= kunde.subdomain %></td>
        <td><span class="status-<%= kunde.status %>"><%= kunde.status %></span></td>
        <td><%= new Date(kunde.erstellungsdatum).toLocaleDateString() %></td>
        <td>
          <a href="/admin/kunden/<%= kunde.id %>" class="btn">Details</a>
        </td>
      </tr>
      <% }); %>
    </tbody>
  </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
  // Chart.js-Code für Übersichtsdiagramme
</script>

<%- include('partials/footer') %>
```

## Option 2: Separate Admin-Dashboard-Anwendung (für größere Systeme)

Wenn Ihr System wächst, könnte ein separates Admin-Dashboard besser sein:

### Vorteile:
- Unabhängig von der Haupt-API-Anwendung
- Kann separat skaliert werden
- Bessere Trennung von Zuständigkeiten

### Technologie-Stack:
- **Next.js** für das Dashboard (einfache Entwicklung, gutes Performance)
- **Vercel** für das Hosting (kostenfreier Tier verfügbar)
- **Prisma** als ORM für Datenbankzugriff
- **NextAuth.js** für Authentifizierung
- **SWR** für Daten-Fetching
- **Recharts** oder **Chart.js** für Visualisierungen

Diese Option ist komplexer, aber bietet mehr Flexibilität und bessere Benutzererfahrung.

## Option 3: Low-Code Dashboard mit vorhandenen Tools

### Vorteile:
- Schnelle Implementierung
- Minimaler Code erforderlich
- Fertige Komponenten und Vorlagen

### Optionen:
- **Retool**: Verbindung zur PostgreSQL-DB, Drag-and-Drop-Dashboard-Erstellung
- **Appsmith**: Open-Source-Alternative zu Retool, kann selbst gehostet werden
- **Forest Admin**: Automatisch generiertes Admin-Panel für Ihre Datenbank
- **PopSQL/Metabase**: Für einfache Datenanalyse und -visualisierung

## Empfehlung für Ihr Start-up

Für ein kosteneffizientes Start-up empfehle ich **Option 1 (integriertes Dashboard)** mit folgenden Funktionen:

1. **Hauptübersicht**:
   - Gesamttoken und Kosten (täglich/wöchentlich/monatlich)
   - Aktive Kunden
   - Top-Kunden nach Nutzung

2. **Kundendetails**:
   - Nutzungsverlauf (Diagramm)
   - API-Anfragen-Historie
   - Kosten pro Monat

3. **Verwaltungsfunktionen**:
   - Neuen Kunden anlegen (generiert Subdomain und API-Key)
   - Kunden aktivieren/deaktivieren
   - Limits pro Kunde festlegen

4. **Abrechnungsübersicht**:
   - Monatliche Rechnungsübersicht pro Kunde
   - Exportfunktion für Abrechnungsdaten

Diese Lösung lässt sich mit minimalem Aufwand und ohne zusätzliche Infrastrukturkosten umsetzen, da sie direkt in Ihre bestehende Render.com-Anwendung integriert wird.

Wenn Ihr Unternehmen wächst, können Sie später problemlos zu Option 2 oder 3 migrieren.