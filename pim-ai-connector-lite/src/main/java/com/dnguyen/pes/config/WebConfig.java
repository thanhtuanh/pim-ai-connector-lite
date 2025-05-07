package com.dnguyen.pes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${WEB_DOMAIN:localhost}")
    private String webDomain;

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    private final ApiKeyInterceptor apiKeyInterceptor;

    public WebConfig(ApiKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**") // API-Pfade schützen
                .excludePathPatterns("/api/health", "/api/info"); // Diese Endpunkte ausschließen falls benötigt
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Erkennen, ob wir auf render.com laufen und entsprechende CORS-Konfiguration
        // vornehmen
        if ("pim-ai-connector-lite.onrender.com".equals(webDomain)) {
            // Konfiguration für render.com
            registry.addMapping("/**")
                    .allowedOrigins(
                            "https://pim-ai-connector-lite.onrender.com",
                            "https://*.onrender.com")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .maxAge(3600);

            logger.info("CORS für render.com Domain konfiguriert: {}", webDomain);
        } else {
            // Lokale Entwicklungsumgebung oder andere Umgebung
            registry.addMapping("/**")
                    .allowedOrigins("*") // Im Entwicklungsmodus alles erlauben
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .maxAge(3600);

            logger.info("CORS für Entwicklungsumgebung konfiguriert: {}", webDomain);
        }
    }
}