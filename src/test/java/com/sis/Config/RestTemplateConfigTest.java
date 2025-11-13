package com.sis.Config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {

    @InjectMocks
    private RestTemplateConfig restTemplateConfig;

    // ========== TEST 1: Bean de RestTemplate se crea correctamente ==========
    @Test
    void restTemplate_deberiaCrearBean() {
        // Act
        RestTemplate restTemplate = restTemplateConfig.restTemplate();

        // Assert
        assertNotNull(restTemplate, "El bean de RestTemplate no debe ser nulo");
    }

    // ========== TEST 2: Bean devuelve una instancia válida de RestTemplate ==========
    @Test
    void restTemplate_deberiaRetornarInstanciaDeRestTemplate() {
        // Act
        RestTemplate restTemplate = restTemplateConfig.restTemplate();

        // Assert
        assertInstanceOf(RestTemplate.class, restTemplate,
                "Debe retornar una instancia de RestTemplate");
    }

    // ========== TEST 3: Cada llamada crea una nueva instancia ==========
    @Test
    void restTemplate_cadaLlamadaDeberiaCrearNuevaInstancia() {
        // Act
        RestTemplate restTemplate1 = restTemplateConfig.restTemplate();
        RestTemplate restTemplate2 = restTemplateConfig.restTemplate();

        // Assert
        assertNotSame(restTemplate1, restTemplate2,
                "Cada llamada al método debe crear una nueva instancia");
    }

    // ========== TEST 4: RestTemplate creado está completamente inicializado ==========
    @Test
    void restTemplate_deberiaEstarCompletamenteInicializado() {
        // Act
        RestTemplate restTemplate = restTemplateConfig.restTemplate();

        // Assert
        assertNotNull(restTemplate.getMessageConverters(),
                "Los message converters deben estar inicializados");
        assertFalse(restTemplate.getMessageConverters().isEmpty(),
                "Debe tener message converters por defecto");
        assertNotNull(restTemplate.getRequestFactory(),
                "El request factory debe estar inicializado");
    }
}