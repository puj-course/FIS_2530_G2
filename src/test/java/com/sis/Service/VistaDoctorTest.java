package com.sis.Service;

import com.sis.Model.Enum.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VistaDoctorTest {

    @Mock
    private SmsService smsService;

    @InjectMocks
    private VistaDoctor vistaDoctor;

    @BeforeEach
    void setUp() {
        // El mock de SmsService ya está inyectado automáticamente
        when(smsService.enviarMensaje(anyString(), anyString())).thenReturn(true);
    }

    // ========== TEST 1: Evento NUEVO_TRIAGE ==========
    @Test
    void actualizar_cuandoEsNuevoTriage_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Paciente: Juan Pérez - Prioridad: Alta";

        // Act
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("Nuevo Triage registrado")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 2: Evento DIAGNOSTICO_LISTO ==========
    @Test
    void actualizar_cuandoEsDiagnosticoListo_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Paciente: María López - Diagnóstico: Gripe";

        // Act
        vistaDoctor.actualizar(TipoEvento.DIAGNOSTICO_LISTO, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("Diagnostico listo para revisión")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 3: Evento CITA_CONFIRMADA ==========
    @Test
    void actualizar_cuandoEsCitaConfirmada_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Cita: 2024-12-01 10:00 - Paciente: Carlos Ruiz";

        // Act
        vistaDoctor.actualizar(TipoEvento.CITA_CONFIRMADA, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("Cita confirmada para un paciente")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 4: Evento NO relevante (default) ==========
    @Test
    void actualizar_cuandoEventoNoRelevante_deberiaEnviarMensajeGenerico() {
        // Arrange - Evento que no es relevante para el doctor
        String datos = "Algún dato";

        // Act
        vistaDoctor.actualizar(TipoEvento.PACIENTE_LLEGADA, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("El evento no es relevante para el doctor")
        );
    }

    // ========== TEST 5: Evento RESULTADO_DISPONIBLE (default) ==========
    @Test
    void actualizar_cuandoEsResultadoDisponible_deberiaEnviarMensajeGenerico() {
        // Arrange
        String datos = "Resultado de laboratorio listo";

        // Act
        vistaDoctor.actualizar(TipoEvento.RESULTADO_DISPONIBLE, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("El evento no es relevante para el doctor")
        );
    }

    // ========== TEST 6: Verificar todos los tipos de eventos ==========
    @Test
    void actualizar_todosLosTiposDeEvento_deberianLlamarSmsService() {
        // Act - Probar con todos los tipos de eventos
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, "Datos 1");
        vistaDoctor.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Datos 2");
        vistaDoctor.actualizar(TipoEvento.CITA_CONFIRMADA, "Datos 3");
        vistaDoctor.actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos 4");
        vistaDoctor.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos 5");

        // Assert - Debe haber enviado 5 mensajes
        verify(smsService, times(5)).enviarMensaje(eq("Doctor"), anyString());
    }

    // ========== TEST 7: Verificar formato del mensaje para NUEVO_TRIAGE ==========
    @Test
    void actualizar_nuevoTriage_deberiaIncluirDatosEnMensaje() {
        // Arrange
        String datosEsperados = "ID: 123, Paciente: Ana";

        // Act
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, datosEsperados);

        // Assert
        verify(smsService).enviarMensaje(
                eq("Doctor"),
                eq("Nuevo Triage registrado" + datosEsperados)
        );
    }

    // ========== TEST 8: Verificar que se llama exactamente una vez por evento ==========
    @Test
    void actualizar_cadaEvento_debeLlamarSmsServiceUnaVez() {
        // Act
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, "Test");

        // Assert
        verify(smsService, times(1)).enviarMensaje(anyString(), anyString());
        verifyNoMoreInteractions(smsService);
    }

    // ========== TEST 9: Datos null no deberían causar error ==========
    @Test
    void actualizar_conDatosNull_noDeberiaLanzarExcepcion() {
        // Act & Assert - No debe lanzar excepción
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, null);

        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("Nuevo Triage registrado")
        );
    }

    // ========== TEST 10: Múltiples actualizaciones consecutivas ==========
    @Test
    void actualizar_multiplesEventosConsecutivos_deberiaEnviarTodosMensajes() {
        // Act
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, "Paciente 1");
        vistaDoctor.actualizar(TipoEvento.NUEVO_TRIAGE, "Paciente 2");
        vistaDoctor.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Resultado A");

        // Assert
        verify(smsService, times(3)).enviarMensaje(eq("Doctor"), anyString());
        verify(smsService, times(2)).enviarMensaje(
                eq("Doctor"),
                contains("Nuevo Triage registrado")
        );
        verify(smsService, times(1)).enviarMensaje(
                eq("Doctor"),
                contains("Diagnostico listo para revisión")
        );
    }
}