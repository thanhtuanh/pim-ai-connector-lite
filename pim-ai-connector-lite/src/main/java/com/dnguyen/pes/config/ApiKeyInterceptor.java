package com.dnguyen.pes.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyInterceptor.class);

    @Value("${API_KEY:demo-key}")
    private String expectedApiKey;

    @Value("${ENABLE_API_KEY:true}")
    private boolean enableApiKeyAuth;

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String API_KEY_PARAM = "apiKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // Wenn API-Key-Authentifizierung deaktiviert ist, erlaube Anfrage
        if (!enableApiKeyAuth) {
            return true;
        }

        // Hole den API-Key aus dem Header oder Request-Parameter
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = request.getParameter(API_KEY_PARAM);
        }

        // Verifiziere den API-Key
        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            String path = request.getRequestURI();
            logger.warn("Ungültiger API-Key: {} für Pfad: {}", apiKey, path);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            // Je nach Sprache verschiedene Fehlermeldungen
            if (path.contains("/en") || path.endsWith("-en.html")) {
                response.getWriter().write("{\"error\":\"Invalid API key\",\"status\":401}");
            } else {
                response.getWriter().write("{\"error\":\"Ungültiger API-Key\",\"status\":401}");
            }

            return false;
        }

        return true;
    }
}