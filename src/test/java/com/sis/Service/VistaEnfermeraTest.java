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
class VistaEnfermeraTest {

    @Mock
    private SmsService smsService;

    @InjectMocks
    private VistaEnfermera vistaEnfermera;

    @BeforeEach
    void setUp() {
        when(smsService.enviarMensaje(anyString(), anyString())).thenReturn(true);
    }

    // ========== TEST 1: Evento PACIENTE_LLEGADA ==========
    @Test
    void actualizar_cuandoEsPacienteLlegada_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Paciente: Juan Pérez - Hora: 10:30";

        // Act
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("Nuevo paciente llegó al hospital")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 2: Evento NUEVO_TRIAGE ==========
    @Test
    void actualizar_cuandoEsNuevoTriage_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Paciente: María López - ID: 456";

        // Act
        vistaEnfermera.actualizar(TipoEvento.NUEVO_TRIAGE, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("Se debe realizar un triage al nuevo paciente")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 3: Evento NO relevante (DIAGNOSTICO_LISTO) ==========
    @Test
    void actualizar_cuandoEsDiagnosticoListo_deberiaEnviarMensajeGenerico() {
        // Arrange
        String datos = "Diagnóstico disponible";

        // Act
        vistaEnfermera.actualizar(TipoEvento.DIAGNOSTICO_LISTO, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("El evento no es relevante para la enfermera")
        );
    }

    // ========== TEST 4: Evento NO relevante (CITA_CONFIRMADA) ==========
    @Test
    void actualizar_cuandoEsCitaConfirmada_deberiaEnviarMensajeGenerico() {
        // Arrange
        String datos = "Cita confirmada";

        // Act
        vistaEnfermera.actualizar(TipoEvento.CITA_CONFIRMADA, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("El evento no es relevante para la enfermera")
        );
    }

    // ========== TEST 5: Evento NO relevante (RESULTADO_DISPONIBLE) ==========
    @Test
    void actualizar_cuandoEsResultadoDisponible_deberiaEnviarMensajeGenerico() {
        // Arrange
        String datos = "Resultado de laboratorio";

        // Act
        vistaEnfermera.actualizar(TipoEvento.RESULTADO_DISPONIBLE, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("El evento no es relevante para la enfermera")
        );
    }

    // ========== TEST 6: Verificar todos los tipos de eventos ==========
    @Test
    void actualizar_todosLosTiposDeEvento_deberianLlamarSmsService() {
        // Act - Probar con todos los tipos de eventos
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos 1");
        vistaEnfermera.actualizar(TipoEvento.NUEVO_TRIAGE, "Datos 2");
        vistaEnfermera.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Datos 3");
        vistaEnfermera.actualizar(TipoEvento.CITA_CONFIRMADA, "Datos 4");
        vistaEnfermera.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos 5");

        // Assert - Debe haber enviado 5 mensajes (2 relevantes + 3 no relevantes)
        verify(smsService, times(5)).enviarMensaje(eq("Enfermera"), anyString());
    }

    // ========== TEST 7: Verificar formato del mensaje para PACIENTE_LLEGADA ==========
    @Test
    void actualizar_pacienteLlegada_deberiaIncluirDatosEnMensaje() {
        // Arrange
        String datosEsperados = "ID: 789, Nombre: Carlos";

        // Act
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, datosEsperados);

        // Assert
        verify(smsService).enviarMensaje(
                eq("Enfermera"),
                eq("Nuevo paciente llegó al hospital " + datosEsperados)
        );
    }

    // ========== TEST 8: Verificar formato del mensaje para NUEVO_TRIAGE ==========
    @Test
    void actualizar_nuevoTriage_deberiaIncluirDatosEnMensaje() {
        // Arrange
        String datosEsperados = "Paciente urgente - Prioridad Alta";

        // Act
        vistaEnfermera.actualizar(TipoEvento.NUEVO_TRIAGE, datosEsperados);

        // Assert
        verify(smsService).enviarMensaje(
                eq("Enfermera"),
                eq("Se debe realizar un triage al nuevo paciente " + datosEsperados)
        );
    }

    // ========== TEST 9: Verificar que se llama exactamente una vez por evento ==========
    @Test
    void actualizar_cadaEvento_debeLlamarSmsServiceUnaVez() {
        // Act
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Test");

        // Assert
        verify(smsService, times(1)).enviarMensaje(anyString(), anyString());
        verifyNoMoreInteractions(smsService);
    }

    // ========== TEST 10: Datos null no deberían causar error ==========
    @Test
    void actualizar_conDatosNull_noDeberiaLanzarExcepcion() {
        // Act & Assert - No debe lanzar excepción
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, null);

        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("Nuevo paciente llegó al hospital")
        );
    }

    // ========== TEST 11: Múltiples llegadas de pacientes ==========
    @Test
    void actualizar_multiplesLlegadasDePacientes_deberiaEnviarTodosMensajes() {
        // Act
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Paciente 1");
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Paciente 2");
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Paciente 3");

        // Assert
        verify(smsService, times(3)).enviarMensaje(
                eq("Enfermera"),
                contains("Nuevo paciente llegó al hospital")
        );
    }

    // ========== TEST 12: Eventos relevantes vs no relevantes ==========
    @Test
    void actualizar_eventosRelevantes_deberianEnviarMensajesEspecificos() {
        // Act - Eventos relevantes para enfermera
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos 1");
        vistaEnfermera.actualizar(TipoEvento.NUEVO_TRIAGE, "Datos 2");

        // Assert - Verificar que NO se enviaron mensajes genéricos
        verify(smsService, never()).enviarMensaje(
                anyString(),
                contains("no es relevante")
        );
        verify(smsService, times(2)).enviarMensaje(eq("Enfermera"), anyString());
    }

    // ========== TEST 13: Eventos no relevantes solo envían mensaje genérico ==========
    @Test
    void actualizar_eventosNoRelevantes_deberianEnviarMensajeGenerico() {
        // Act - Eventos NO relevantes para enfermera
        vistaEnfermera.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Datos 1");
        vistaEnfermera.actualizar(TipoEvento.CITA_CONFIRMADA, "Datos 2");
        vistaEnfermera.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos 3");

        // Assert - Todos deben ser mensajes genéricos
        verify(smsService, times(3)).enviarMensaje(
                eq("Enfermera"),
                eq("El evento no es relevante para la enfermera.")
        );
    }

    // ========== TEST 14: Secuencia típica de trabajo de enfermera ==========
    @Test
    void actualizar_secuenciaTipica_pacienteLlegaYLuegoTriage() {
        // Act - Secuencia típica: paciente llega -> se hace triage
        vistaEnfermera.actualizar(TipoEvento.PACIENTE_LLEGADA, "Juan Pérez");
        vistaEnfermera.actualizar(TipoEvento.NUEVO_TRIAGE, "Juan Pérez");

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("Nuevo paciente llegó")
        );
        verify(smsService, times(1)).enviarMensaje(
                eq("Enfermera"),
                contains("Se debe realizar un triage")
        );
        verify(smsService, times(2)).enviarMensaje(anyString(), anyString());
    }
}