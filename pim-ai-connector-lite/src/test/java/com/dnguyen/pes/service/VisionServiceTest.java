

package com.dnguyen.pes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class VisionServiceTest {

    @Mock
    private ConfigService configService;

    @Mock
    private LanguageConfigService languageConfigService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VisionService visionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnalyzeImage_SuccessfulResponse() {
        byte[] imageBytes = "test-image".getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> responseBody = Map.of(
            "responses", List.of(
                Map.of("fullTextAnnotation", Map.of("text", "Detected Text"))
            )
        );

        ResponseEntity<Map> mockResponse = mock(ResponseEntity.class);
        when(mockResponse.getBody()).thenReturn(responseBody);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        String result = visionService.analyzeImage(imageBytes);

        assertEquals("Detected Text", result);
    }

    @Test
    void testAnalyzeImage_NoTextDetected() {
        byte[] imageBytes = "test-image".getBytes();

        Map<String, Object> responseBody = Map.of(
            "responses", List.of(
                Map.of("fullTextAnnotation", null)
            )
        );

        ResponseEntity<Map> mockResponse = mock(ResponseEntity.class);
        when(mockResponse.getBody()).thenReturn(responseBody);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        when(languageConfigService.getDefaultLanguage()).thenReturn("en");

        String result = visionService.analyzeImage(imageBytes);

        assertEquals("⚠️ No text detected in the image.", result);
    }

    @Test
    void testAnalyzeImage_ErrorDuringRequest() {
        byte[] imageBytes = "test-image".getBytes();

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("API error"));
        when(languageConfigService.getDefaultLanguage()).thenReturn("en");

        String result = visionService.analyzeImage(imageBytes);

        assertTrue(result.contains("❌ Error during image analysis: API error"));
    }

    @Test
    void testGenerateGptDescription_SuccessfulResponse() {
        Map<String, String> paramValues = Map.of("key", "value");
        String prompt = "Generated Prompt";
        String systemMessage = "System Message";

        Map<String, Object> responseBody = Map.of(
            "choices", List.of(
                Map.of("message", Map.of("content", "Generated Description"))
            )
        );

        ResponseEntity<Map> mockResponse = mock(ResponseEntity.class);
        when(mockResponse.getBody()).thenReturn(responseBody);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        when(configService.generatePrompt(paramValues)).thenReturn(prompt);
        when(languageConfigService.getDefaultLanguage()).thenReturn("en");
        when(languageConfigService.getPromptSuffix("en")).thenReturn("Suffix");
        when(languageConfigService.getSystemMessage("en")).thenReturn(systemMessage);

        String result = visionService.generateGptDescription(paramValues);

        assertEquals("Generated Description", result);
    }

    @Test
    void testGenerateGptDescription_NoResponse() {
        Map<String, String> paramValues = Map.of("key", "value");

        Map<String, Object> responseBody = Map.of("choices", List.of());

        ResponseEntity<Map> mockResponse = mock(ResponseEntity.class);
        when(mockResponse.getBody()).thenReturn(responseBody);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        when(configService.generatePrompt(paramValues)).thenReturn("Generated Prompt");
        when(languageConfigService.getDefaultLanguage()).thenReturn("en");
        when(languageConfigService.getPromptSuffix("en")).thenReturn("Suffix");
        when(languageConfigService.getSystemMessage("en")).thenReturn("System Message");

        String result = visionService.generateGptDescription(paramValues);

        assertEquals("⚠️ No response received from GPT.", result);
    }

    @Test
    void testGenerateGptDescription_ErrorDuringRequest() {
        Map<String, String> paramValues = Map.of("key", "value");

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenThrow(new RuntimeException("API error"));
        when(configService.generatePrompt(paramValues)).thenReturn("Generated Prompt");
        when(languageConfigService.getDefaultLanguage()).thenReturn("en");
        when(languageConfigService.getPromptSuffix("en")).thenReturn("Suffix");
        when(languageConfigService.getSystemMessage("en")).thenReturn("System Message");

        String result = visionService.generateGptDescription(paramValues);

        assertTrue(result.contains("❌ Error in GPT request: API error"));
    }
}