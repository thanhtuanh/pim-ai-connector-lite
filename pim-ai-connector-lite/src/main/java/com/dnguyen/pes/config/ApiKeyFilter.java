package com.dnguyen.pes.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Value("${API_KEY:demo-key}")
    private String expectedApiKey;

    @Value("${ENABLE_API_KEY:true}")
    private boolean enableApiKeyAuth;

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String API_KEY_PARAM = "apiKey";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Wenn API-Key-Authentifizierung deaktiviert ist, filter überspringen
        if (!enableApiKeyAuth) {
            filterChain.doFilter(request, response);
            return;
        }

        // Prüfe, ob es sich um eine statische Ressource oder HTML handelt
        String path = request.getRequestURI();
        if (isStaticResource(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Hole den API-Key aus dem Header oder Request-Parameter
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = request.getParameter(API_KEY_PARAM);
        }

        // Verifiziere den API-Key
        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            logger.warn("Ungültiger API-Key: {} für Pfad: {}", apiKey, path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Ungültiger API-Key\",\"status\":401}");
            return;
        }

        // Wenn der API-Key gültig ist, fahre mit der nächsten Filter-Chain fort
        filterChain.doFilter(request, response);
    }

    /**
     * Prüft, ob es sich um eine statische Ressource oder die Haupt-HTML handelt,
     * die ohne Authentifizierung zugänglich sein sollte
     */
    private boolean isStaticResource(String path) {
        // Erlaube Zugriff auf statische Ressourcen und HTML-Dateien
        return path.equals("/")
                || path.startsWith("/assets/")
                || path.endsWith(".html")
                || path.endsWith(".js")
                || path.endsWith(".css")
                || path.endsWith(".ico")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".gif")
                || path.endsWith(".svg");
    }
}