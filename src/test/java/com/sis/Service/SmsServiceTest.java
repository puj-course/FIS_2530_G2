package com.sis.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private RestTemplate restTemplate; // ← Mockear RestTemplate

    @InjectMocks
    private SmsService smsService; // ← Se inyecta el mock automáticamente

    private final String TEST_BOT_TOKEN = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11";
    private final String TEST_CHAT_ID = "123456789";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsService, "botToken", TEST_BOT_TOKEN);
        ReflectionTestUtils.setField(smsService, "chatId", TEST_CHAT_ID);
    }

    // ========== TEST 1: Envío exitoso ==========
    @Test
    void enviarMensaje_cuandoEsExitoso_deberiaRetornarTrue() {
        // Arrange
        ResponseEntity<String> responseOk = new ResponseEntity<>("{\"ok\":true}", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseOk);

        // Act
        boolean resultado = smsService.enviarMensaje("+573001234567", "Test");

        // Assert
        assertTrue(resultado);
        assertEquals(1, smsService.getMensajesEnviados());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    // ========== TEST 2: Incrementar contador ==========
    @Test
    void enviarMensaje_deberiaIncrementarContador() {
        // Arrange
        ResponseEntity<String> responseOk = new ResponseEntity<>("{\"ok\":true}", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseOk);

        // Act
        smsService.enviarMensaje("+573001234567", "Mensaje 1");
        smsService.enviarMensaje("+573007654321", "Mensaje 2");

        // Assert
        assertEquals(2, smsService.getMensajesEnviados());
    }

    // ========== TEST 3: Error con status diferente de OK ==========
    @Test
    void enviarMensaje_cuandoStatusNoEsOK_deberiaRetornarFalse() {
        // Arrange
        ResponseEntity<String> responseBadRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseBadRequest);

        // Act
        boolean resultado = smsService.enviarMensaje("+573001234567", "Test");

        // Assert
        assertFalse(resultado);
        assertEquals(0, smsService.getMensajesEnviados());
    }

    // ========== TEST 4: Excepción no rompe la aplicación ==========
    @Test
    void enviarMensaje_cuandoLanzaExcepcion_deberiaRetornarFalse() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Error de conexión"));

        // Act
        boolean resultado = smsService.enviarMensaje("+573001234567", "Test");

        // Assert
        assertFalse(resultado);
        assertEquals(0, smsService.getMensajesEnviados());
    }

    // ========== TEST 5: Contador inicial ==========
    @Test
    void getMensajesEnviados_deberiaEmpezarEnCero() {
        assertEquals(0, smsService.getMensajesEnviados());
    }

    // ========== TEST 6: Solo cuenta envíos exitosos ==========
    @Test
    void getMensajesEnviados_soloIncrementaConEnviosExitosos() {
        // Arrange
        ResponseEntity<String> responseOk = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<String> responseError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseOk)      // Exitoso
                .thenReturn(responseError)   // Falla
                .thenReturn(responseOk);     // Exitoso

        // Act
        smsService.enviarMensaje("+573001234567", "Mensaje 1");
        smsService.enviarMensaje("+573001234567", "Mensaje 2");
        smsService.enviarMensaje("+573001234567", "Mensaje 3");

        // Assert
        assertEquals(2, smsService.getMensajesEnviados());
    }

    // ========== TEST 7: Múltiples envíos ==========
    @Test
    void enviarMultiplesMensajes_todosExitosos() {
        // Arrange
        ResponseEntity<String> responseOk = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseOk);

        // Act & Assert
        assertTrue(smsService.enviarMensaje("+573001111111", "Mensaje 1"));
        assertTrue(smsService.enviarMensaje("+573002222222", "Mensaje 2"));
        assertTrue(smsService.enviarMensaje("+573003333333", "Mensaje 3"));
        assertEquals(3, smsService.getMensajesEnviados());
    }
}