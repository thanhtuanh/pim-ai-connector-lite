// API-Key aus localStorage laden oder Standardwert verwenden
let API_KEY = localStorage.getItem('pim_ai_api_key') || "demo-key";

// DOM-Elemente
const form = document.getElementById("analyzeForm");
const parameterForm = document.getElementById("parameterForm");
const produktTypInfo = document.getElementById("produktTypInfo");
const responseArea = document.getElementById("responseArea");
const tabText = document.getElementById("tab-text");
const tabImage = document.getElementById("tab-image");
const imageUploadSection = document.getElementById("image-upload-section");
const dropArea = document.getElementById("dropArea");
const fileInput = document.getElementById("fileInput");
const imagePreview = document.getElementById("imagePreview");
const imageStatus = document.getElementById("imageStatus");
const resetBtn = document.getElementById("resetBtn");
const generateBtn = document.getElementById("generateBtn");
const productTypeSelect = document.getElementById("product-type-select");
const languageSelect = document.getElementById("language-select");
const languageSwitchBtn = document.getElementById("language-switch-btn");
const saveApiKeyBtn = document.getElementById("save-api-key");

// Globale Variablen
let isImageAnalysisActive = false;
let base64Image = "";
let currentLanguage = "de"; // Standard-Sprache
let produktTypen = [];
let demoDaten = {}; // Wird aus XML-Konfiguration geladen
let activeProduktTypId = "fashion";

// Event-Listener beim Laden der Seite
document.addEventListener('DOMContentLoaded', function () {
    // API-Key-Element im Header aktualisieren
    document.getElementById('api-key-input').value = API_KEY;
    
    // Produkttypen und Demo-Daten laden
    loadProductTypes().catch(error => {
        console.error("Fehler beim Initialisieren der Anwendung:", error);
    });
});

// API-Key speichern, wenn der Speichern-Button geklickt wird
saveApiKeyBtn.addEventListener("click", function () {
    const newApiKey = document.getElementById("api-key-input").value;
    if (newApiKey && newApiKey.trim() !== '') {
        API_KEY = newApiKey.trim();
        localStorage.setItem('pim_ai_api_key', API_KEY);
        alert(currentLanguage === "de" ? "API-Key gespeichert!" : "API key saved!");
    }
});

// Tab-Wechsel Funktionalität
tabText.addEventListener("click", function () {
    tabText.classList.add("active");
    tabImage.classList.remove("active");
    imageUploadSection.style.display = "none";
    isImageAnalysisActive = false;
    updateGenerateButtonText();
});

tabImage.addEventListener("click", function () {
    tabText.classList.remove("active");
    tabImage.classList.add("active");
    imageUploadSection.style.display = "block";
    isImageAnalysisActive = true;
    updateGenerateButtonText();
});

// Drag & Drop und Bildupload
dropArea.addEventListener("click", function () {
    fileInput.click();
});

fileInput.addEventListener("change", function () {
    handleFiles(this.files);
});

dropArea.addEventListener("dragover", function (e) {
    e.preventDefault();
    dropArea.style.borderColor = "#3498db";
});

dropArea.addEventListener("dragleave", function () {
    dropArea.style.borderColor = "#ccc";
});

dropArea.addEventListener("drop", function (e) {
    e.preventDefault();
    dropArea.style.borderColor = "#ccc";
    handleFiles(e.dataTransfer.files);
});

// Zurücksetzen-Button
resetBtn.addEventListener("click", function () {
    form.reset();
    imagePreview.style.display = "none";
    base64Image = "";
    imageStatus.textContent = "";
    responseArea.textContent = currentLanguage === "de"
        ? "Formular zurückgesetzt. Noch keine Anfrage gesendet."
        : "Form reset. No request sent yet.";

    // Lade Demo-Daten für den aktuellen Produkttyp
    loadDemoDataForProductType(activeProduktTypId);
});

// Produkttyp-Auswahl Event-Listener
productTypeSelect.addEventListener("change", function () {
    activeProduktTypId = this.value;
    loadDemoDataForProductType(activeProduktTypId);

    // Aktualisiere die Produkttyp-Info
    const selectedOption = this.options[this.selectedIndex];
    const produktTypName = selectedOption.text;
    updateProduktTypInfo(activeProduktTypId, produktTypName);
});

// Sprachumschaltung
languageSwitchBtn.addEventListener("click", function () {
    const newLanguage = languageSelect.value;
    changeLanguage(newLanguage);
});

// Formular-Submission
form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Parameter-Werte sammeln
    const formData = new FormData(form);
    const payload = {};

    // Alle Eingabefelder im Formular durchgehen
    for (const [key, value] of formData.entries()) {
        payload[key] = value;
    }

    // Base64-Bild hinzufügen, wenn vorhanden
    payload.imageBase64 = base64Image;

    // Anzeigen, dass die Anfrage gesendet wird
    responseArea.textContent = currentLanguage === "de"
        ? "⏳ Anfrage wird gesendet..."
        : "⏳ Sending request...";

    // Entscheiden, welcher Endpunkt verwendet wird
    if (isImageAnalysisActive && base64Image) {
        sendRequestWithImage(payload);
    } else {
        sendRequest(payload);
    }
});

// Funktion zum Aktualisieren des Texts auf dem Generieren-Button je nach Sprache und Modus
function updateGenerateButtonText() {
    if (currentLanguage === "de") {
        generateBtn.textContent = isImageAnalysisActive
            ? "Bild analysieren & Beschreibung generieren"
            : "Beschreibung generieren";
    } else {
        generateBtn.textContent = isImageAnalysisActive
            ? "Analyze Image & Generate Description"
            : "Generate Description";
    }
}

// Funktion zum Hinzufügen des API-Keys zu allen Fetch-Anfragen
async function fetchWithApiKey(url, options = {}) {
    // Stelle sicher, dass headers existiert
    if (!options.headers) {
        options.headers = {};
    }

    // Füge den API-Key-Header hinzu
    options.headers['X-API-Key'] = API_KEY;

    try {
        const response = await fetch(url, options);

        // Prüfe auf Unauthorized-Fehler (401)
        if (response.status === 401) {
            const errorData = await response.json();
            // Zeige Fehlermeldung an
            alert(errorData.error || "API-Schlüssel ungültig oder fehlt");
            throw new Error("API-Authentifizierungsfehler");
        }

        return response;
    } catch (error) {
        console.error("Fehler bei der API-Anfrage:", error);
        throw error;
    }
}

// Bilder verarbeiten
function handleFiles(files) {
    if (files.length > 0) {
        const file = files[0];
        if (!file.type.match('image.*')) {
            imageStatus.textContent = currentLanguage === "de"
                ? "❌ Bitte nur Bilddateien hochladen (JPG, PNG, etc.)"
                : "❌ Please upload image files only (JPG, PNG, etc.)";
            return;
        }

        // Größenbeschränkung (5MB)
        if (file.size > 5 * 1024 * 1024) {
            imageStatus.textContent = currentLanguage === "de"
                ? "❌ Die Bilddatei ist zu groß (maximal 5 MB erlaubt)"
                : "❌ The image file is too large (maximum 5 MB allowed)";
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            imagePreview.src = e.target.result;
            imagePreview.style.display = "block";
            base64Image = e.target.result.split(',')[1]; // Base64 ohne MIME-Typ-Header
            imageStatus.textContent = currentLanguage === "de"
                ? "✅ Bild erfolgreich geladen: " + file.name
                : "✅ Image successfully loaded: " + file.name;
        };
        reader.readAsDataURL(file);
    }
}

// Sprache ändern
function changeLanguage(language) {
    // API-Aufruf, um die Sprache zu ändern
    fetchWithApiKey(`/api/language/set?language=${language}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                currentLanguage = language;

                // UI-Texte aktualisieren
                updateUITexts();

                // Info-Box aktualisieren
                const selectedOption = productTypeSelect.options[productTypeSelect.selectedIndex];
                updateProduktTypInfo(activeProduktTypId, selectedOption.text);

                // Button-Text aktualisieren
                updateGenerateButtonText();
            } else {
                alert(currentLanguage === "de"
                    ? "Fehler beim Ändern der Sprache: " + data.message
                    : "Error changing language: " + data.message);
            }
        })
        .catch(error => {
            console.error("Sprachänderung fehlgeschlagen:", error);
            alert(currentLanguage === "de"
                ? "Fehler bei der Sprachänderung. Bitte versuchen Sie es später erneut."
                : "Error changing language. Please try again later.");
        });
}

// UI-Texte aktualisieren
function updateUITexts() {
    if (currentLanguage === "de") {
        document.querySelector("label[for='language-select']").textContent = "Sprache wählen:";
        document.querySelector("label[for='product-type-select']").textContent = "Produkttyp auswählen:";
        document.querySelector("#tab-text").textContent = "Nur Text";
        document.querySelector("#tab-image").textContent = "Mit Bildanalyse";
        document.querySelector("#dropArea p").textContent = "📷 Produktbild hier ablegen oder klicken zum Auswählen";
        document.querySelector(".result-container h3").textContent = "Ergebnis:";
        document.querySelector("#language-switch-btn").textContent = "Sprache anwenden";
        document.querySelector("#resetBtn").textContent = "Zurücksetzen";
        document.getElementById("save-api-key").textContent = "Speichern";

        if (responseArea.textContent.includes("Form reset") || responseArea.textContent.includes("No request")) {
            responseArea.textContent = "Noch keine Anfrage gesendet.";
        }
    } else {
        document.querySelector("label[for='language-select']").textContent = "Select language:";
        document.querySelector("label[for='product-type-select']").textContent = "Select product type:";
        document.querySelector("#tab-text").textContent = "Text Only";
        document.querySelector("#tab-image").textContent = "With Image Analysis";
        document.querySelector("#dropArea p").textContent = "📷 Drop product image here or click to select";
        document.querySelector(".result-container h3").textContent = "Result:";
        document.querySelector("#language-switch-btn").textContent = "Apply Language";
        document.querySelector("#resetBtn").textContent = "Reset";
        document.getElementById("save-api-key").textContent = "Save";

        if (responseArea.textContent.includes("Formular zurückgesetzt") || responseArea.textContent.includes("Noch keine")) {
            responseArea.textContent = "No request sent yet.";
        }
    }

    updateGenerateButtonText();
}

// XML-Konfiguration laden
async function loadXMLConfig() {
    try {
        // Zuerst versuchen, über den API-Endpunkt zu laden
        const response = await fetchWithApiKey('/api/vision/xml-config');
        const xmlText = await response.text();
        return await parseXMLConfig(xmlText);
    } catch (apiError) {
        console.log("API-Zugriff fehlgeschlagen, versuche direkten Zugriff:", apiError);
        
        // Fallback: Direkten Zugriff auf die XML-Datei versuchen
        try {
            const directResponse = await fetch('/pim-config.xml');
            if (!directResponse.ok) {
                throw new Error(`HTTP Error: ${directResponse.status}`);
            }
            const xmlText = await directResponse.text();
            return await parseXMLConfig(xmlText);
        } catch (directError) {
            console.error("Auch direkter Zugriff fehlgeschlagen:", directError);
            // Fallback zu fest eingebauten Produkttypen
            return [
                { id: "fashion", name: "Bekleidung" },
                { id: "electronics", name: "Elektronik" },
                { id: "furniture", name: "Möbel" },
                { id: "books", name: "Bücher" }
            ];
        }
    }
}

// XML-Konfiguration parsen
async function parseXMLConfig(xmlText) {
    const parser = new DOMParser();
    const xmlDoc = parser.parseFromString(xmlText, "application/xml");
    
    if (xmlDoc.getElementsByTagName("parsererror").length > 0) {
        console.error("XML-Parsing-Fehler:", xmlDoc.getElementsByTagName("parsererror")[0].textContent);
        throw new Error("XML-Parsing-Fehler");
    }
    
    // Extrahieren von Produkttypen
    const types = Array.from(xmlDoc.querySelectorAll("produkttyp"));
    const resultTypes = [];
    
    // Demo-Daten komplett neu aus XML-Konfiguration laden
    demoDaten = {}; // Zurücksetzen
    
    for (const type of types) {
        const id = type.getAttribute("id");
        const name = type.querySelector("n").textContent.trim();
        resultTypes.push({ id, name });
        
        const demoNode = type.querySelector("demo");
        if (demoNode) {
            // Neues Objekt für diesen Produkttyp erstellen
            const demoData = {};
            
            for (let i = 1; i <= 10; i++) {
                const paramKey = `p${i}`;
                const paramNode = demoNode.querySelector(paramKey);
                if (paramNode && paramNode.textContent) {
                    demoData[paramKey] = paramNode.textContent.trim();
                }
            }
            
            // Demo-Daten für diesen Produkttyp speichern
            demoDaten[id] = demoData;
            console.log(`Demo-Daten für ${id} geladen:`, demoData);
        }
    }

    return resultTypes;
}

// Alle verfügbaren Produkttypen laden
async function loadProductTypes() {
    try {
        // XML-Konfiguration laden einschließlich Demo-Daten
        const types = await loadXMLConfig();
        
        // Falls XML-Laden fehlschlägt, Fallback zur API
        if (!types || types.length === 0) {
            throw new Error("Keine Produkttypen in XML gefunden");
        }
        
        return populateProductTypeSelect(types);
    } catch (xmlError) {
        console.error("Fehler beim Laden der XML-Konfiguration:", xmlError);
        
        try {
            // Fallback: Hole alle Produkttypen über die API
            const response = await fetchWithApiKey('/api/vision/allProductTypes');
            const data = await response.json();
            
            // Produkttypen speichern
            const apiTypes = data.produktTypen || [];
            
            return populateProductTypeSelect(apiTypes);
        } catch (apiError) {
            console.error("Auch API-Zugriff fehlgeschlagen:", apiError);
            
            // Notfall-Optionen, wenn weder XML noch API funktionieren
            const fallbackTypes = [
                { id: "fashion", name: "Bekleidung" },
                { id: "electronics", name: "Elektronik" },
                { id: "furniture", name: "Möbel" },
                { id: "books", name: "Bücher" }
            ];
            
            return populateProductTypeSelect(fallbackTypes);
        }
    }
}

// Dropdown-Menü mit Produkttypen befüllen
function populateProductTypeSelect(types) {
    productTypeSelect.innerHTML = '';
    types.forEach(typ => {
        const option = document.createElement('option');
        option.value = typ.id;
        option.textContent = typ.name;
        // Setze den aktiven Produkttyp als ausgewählt
        if (typ.id === activeProduktTypId) {
            option.selected = true;
        }
        productTypeSelect.appendChild(option);
    });

    // Nach dem Befüllen des Dropdown-Menüs Sprache laden und erste Auswahl aktivieren
    return fetchWithApiKey('/api/language/current')
        .then(response => response.json())
        .then(data => {
            currentLanguage = data.language || "de";
            languageSelect.value = currentLanguage;
            updateUITexts();
            
            // Erste Auswahl aktivieren
            if (productTypeSelect.options.length > 0) {
                const selectedOption = productTypeSelect.options[productTypeSelect.selectedIndex];
                activeProduktTypId = selectedOption.value;
                updateProduktTypInfo(activeProduktTypId, selectedOption.text);
                loadDemoDataForProductType(activeProduktTypId);
            }
            
            return types;
        })
        .catch(err => {
            console.error("Fehler beim Laden der Sprache:", err);
            // Trotzdem erste Auswahl aktivieren
            if (productTypeSelect.options.length > 0) {
                const selectedOption = productTypeSelect.options[productTypeSelect.selectedIndex];
                activeProduktTypId = selectedOption.value;
                updateProduktTypInfo(activeProduktTypId, selectedOption.text);
                loadDemoDataForProductType(activeProduktTypId);
            }
            return types;
        });
}

// Demo-Daten für den ausgewählten Produkttyp laden
function loadDemoDataForProductType(produktTypId) {
    // Default auf den ersten Produkttyp, falls der angeforderte nicht existiert
    if (!demoDaten[produktTypId] && Object.keys(demoDaten).length > 0) {
        produktTypId = Object.keys(demoDaten)[0];
    } else if (!demoDaten[produktTypId]) {
        // Wenn keine Demo-Daten verfügbar sind, von der API holen
        fetchDemoDataFromAPI(produktTypId);
        return;
    }

    // Aktiven Produkttyp speichern
    activeProduktTypId = produktTypId;

    // Demo-Daten für den aktuellen Produkttyp
    const typDemoDaten = demoDaten[produktTypId] || {};

    // Konfiguration für den Produkttyp laden
    fetchWithApiKey(`/api/vision/config?type=${produktTypId}`)
        .then(response => response.json())
        .then(config => {
            // Parameter-Felder aktualisieren
            parameterForm.innerHTML = '';

            for (let i = 1; i <= 10; i++) {
                const paramKey = `p${i}`;
                const paramName = config.parameter[paramKey];

                if (paramName && paramName.trim() !== '') {
                    const demoWert = typDemoDaten[paramKey] || "";
                    const label = document.createElement('label');
                    label.innerHTML = `${paramName}: <input type="text" name="${paramKey}" value="${demoWert}" />`;
                    parameterForm.appendChild(label);
                }
            }

            // Produkttyp-Info aktualisieren
            updateProduktTypInfo(produktTypId, config.produktTypName);
            
            // Falls Demo-Daten in der API-Antwort vorhanden sind, diese auch verwenden
            if (config.demoData && Object.keys(config.demoData).length > 0) {
                demoDaten[produktTypId] = config.demoData;
            }
        })
        .catch(err => {
            console.error("Fehler beim Laden der Konfiguration:", err);

            // Notfall-Felder anzeigen, wenn der Server nicht antwortet
            parameterForm.innerHTML = '';
            if (Object.keys(typDemoDaten).length > 0) {
                for (let i = 1; i <= 10; i++) {
                    const paramKey = `p${i}`;
                    if (typDemoDaten[paramKey]) {
                        const label = document.createElement('label');
                        label.innerHTML = `Parameter ${i}: <input type="text" name="${paramKey}" value="${typDemoDaten[paramKey]}" />`;
                        parameterForm.appendChild(label);
                    }
                }
            } else {
                // Leere Felder anzeigen, wenn keine Demo-Daten verfügbar sind
                for (let i = 1; i <= 5; i++) {
                    const paramKey = `p${i}`;
                    const label = document.createElement('label');
                    label.innerHTML = `Parameter ${i}: <input type="text" name="${paramKey}" value="" />`;
                    parameterForm.appendChild(label);
                }
            }

            updateProduktTypInfo(produktTypId, getProductTypeName(produktTypId));
        });
}

// Demo-Daten direkt von der API holen
async function fetchDemoDataFromAPI(produktTypId) {
    try {
        const response = await fetchWithApiKey(`/api/vision/config?type=${produktTypId}`);
        const config = await response.json();
        
        // Demo-Daten aus der API-Antwort speichern
        if (config.demoData && Object.keys(config.demoData).length > 0) {
            demoDaten[produktTypId] = config.demoData;
        }
        
        // Parameter-Felder aktualisieren
        parameterForm.innerHTML = '';
        
        for (let i = 1; i <= 10; i++) {
            const paramKey = `p${i}`;
            const paramName = config.parameter[paramKey];
            
            if (paramName && paramName.trim() !== '') {
                const demoWert = config.demoData?.[paramKey] || "";
                const label = document.createElement('label');
                label.innerHTML = `${paramName}: <input type="text" name="${paramKey}" value="${demoWert}" />`;
                parameterForm.appendChild(label);
            }
        }
        
        // Produkttyp-Info aktualisieren
        updateProduktTypInfo(produktTypId, config.produktTypName);
    } catch (error) {
        console.error("Fehler beim Abrufen der Demo-Daten von der API:", error);
        
        // Notfall-Felder anzeigen
        parameterForm.innerHTML = '';
        for (let i = 1; i <= 5; i++) {
            const paramKey = `p${i}`;
            const label = document.createElement('label');
            label.innerHTML = `Parameter ${i}: <input type="text" name="${paramKey}" value="" />`;
            parameterForm.appendChild(label);
        }
        
        updateProduktTypInfo(produktTypId, getProductTypeName(produktTypId));
    }
}

// Produkttyp-Namen abrufen
function getProductTypeName(id) {
    // Fallback-Namen für die Produkttypen
    const names = {
        "fashion": "Bekleidung",
        "electronics": "Elektronik",
        "furniture": "Möbel",
        "books": "Bücher"
    };
    return names[id] || id;
}

// Produkttyp-Info aktualisieren
function updateProduktTypInfo(id, name) {
    let infoText;
    if (currentLanguage === "de") {
        infoText = `<strong>Aktiver Produkttyp:</strong> (ID: ${id})`;
    } else {
        infoText = `<strong>Active Product Type:</strong> (ID: ${id})`;
    }
    produktTypInfo.innerHTML = infoText;
}

// Anfrage ohne Bild senden
async function sendRequest(payload) {
    try {
        // Endpunkt mit Sprachparameter
        const endpoint = `/api/enrich/gpt/${currentLanguage}`;

        // GPT-Endpunkt direkt verwenden, da keine Bilder gesendet werden
        const res = await fetchWithApiKey(endpoint, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const text = await res.text();
        responseArea.textContent = text;
    } catch (err) {
        responseArea.textContent = currentLanguage === "de"
            ? "❌ Fehler: " + err.message
            : "❌ Error: " + err.message;
    }
}

// Anfrage mit Bild senden
async function sendRequestWithImage(payload) {
    try {
        // Endpunkt mit Sprachparameter
        const endpoint = `/api/vision/analyzeAndText/${currentLanguage}`;

        // Bild-Analyse-Endpunkt verwenden
        const res = await fetchWithApiKey(endpoint, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const text = await res.text();
        responseArea.textContent = text;
    } catch (err) {
        responseArea.textContent = currentLanguage === "de"
            ? "❌ Fehler bei der Bildanalyse: " + err.message
            : "❌ Error during image analysis: " + err.message;
    }
}

// Hilfelink-Funktionalität - Am Ende der main.js hinzufügen

// Tooltip für API-Key-Hilfe
const apiKeyHelp = document.getElementById('api-key-help');
let tooltip = null;

apiKeyHelp.addEventListener('click', function(e) {
    e.preventDefault();
    
    // Wenn Tooltip bereits existiert, entfernen
    if (tooltip) {
        document.body.removeChild(tooltip);
        tooltip = null;
        return;
    }
    
    // Tooltip erstellen
    tooltip = document.createElement('div');
    tooltip.className = 'tooltip';
    
    // Text je nach Sprache
    if (currentLanguage === 'de') {
        tooltip.innerHTML = `
            <strong>API-Key-Hilfe</strong>
            <p>Der API-Key wird für die Authentifizierung bei API-Anfragen verwendet.</p>
            <p>Für Demozwecke können Sie "demo-key" verwenden.</p>
            <p>Für Produktionsumgebungen benötigen Sie einen eigenen Schlüssel, den Sie nach der Registrierung erhalten.</p>
        `;
    } else {
        tooltip.innerHTML = `
            <strong>API Key Help</strong>
            <p>The API key is used for authentication with API requests.</p>
            <p>For demo purposes, you can use "demo-key".</p>
            <p>For production environments, you need your own key, which you will receive after registration.</p>
        `;
    }
    
    // Position berechnen
    const rect = apiKeyHelp.getBoundingClientRect();
    tooltip.style.top = (rect.bottom + window.scrollY + 10) + 'px';
    tooltip.style.right = (window.innerWidth - rect.right) + 'px';
    
    // Zum DOM hinzufügen
    document.body.appendChild(tooltip);
    
    // Sichtbarkeit verzögern um Animation zu ermöglichen
    setTimeout(() => {
        tooltip.classList.add('visible');
    }, 10);
    
    // Tooltip bei Klick außerhalb schließen
    document.addEventListener('click', closeTooltipOnOutsideClick);
});

// Tooltip schließen bei Klick außerhalb
function closeTooltipOnOutsideClick(e) {
    if (tooltip && !tooltip.contains(e.target) && e.target !== apiKeyHelp) {
        tooltip.classList.remove('visible');
        
        // Tooltip entfernen nach Animation
        setTimeout(() => {
            if (tooltip && tooltip.parentNode) {
                document.body.removeChild(tooltip);
                tooltip = null;
            }
        }, 200);
        
        document.removeEventListener('click', closeTooltipOnOutsideClick);
    }
}