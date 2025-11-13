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
class VistaPacienteTest {

    @Mock
    private SmsService smsService;

    @InjectMocks
    private VistaPaciente vistaPaciente;

    @BeforeEach
    void setUp() {
        when(smsService.enviarMensaje(anyString(), anyString())).thenReturn(true);
    }

    // ========== TEST 1: Evento CITA_CONFIRMADA ==========
    @Test
    void actualizar_cuandoEsCitaConfirmada_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Fecha: 2024-12-15 10:00 - Dr. Pérez";

        // Act
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Su cita ha sido confirmada")
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
        String datos = "Diagnóstico: Gripe común - Tratamiento prescrito";

        // Act
        vistaPaciente.actualizar(TipoEvento.DIAGNOSTICO_LISTO, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Su diagnostico está listo")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 3: Evento RESULTADO_DISPONIBLE ==========
    @Test
    void actualizar_cuandoEsResultadoDisponible_deberiaEnviarMensaje() {
        // Arrange
        String datos = "Examen de sangre - Ver resultados en portal";

        // Act
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Ya puede consultar los resultados de su exámen")
        );
        verify(smsService, times(1)).enviarMensaje(
                anyString(),
                contains(datos)
        );
    }

    // ========== TEST 4: Evento NO relevante (PACIENTE_LLEGADA) ==========
    @Test
    void actualizar_cuandoEsPacienteLlegada_deberiaEnviarMensajeGenerico() {
        // Arrange
        String datos = "Llegada registrada";

        // Act
        vistaPaciente.actualizar(TipoEvento.PACIENTE_LLEGADA, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("El evento no es relevante para el paciente")
        );
    }

    // ========== TEST 5: Evento NO relevante (NUEVO_TRIAGE) ==========
    @Test
    void actualizar_cuandoEsNuevoTriage_deberiaEnviarMensajeGenerico() {
        // Arrange
        String datos = "Triage completado";

        // Act
        vistaPaciente.actualizar(TipoEvento.NUEVO_TRIAGE, datos);

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("El evento no es relevante para el paciente")
        );
    }

    // ========== TEST 6: Verificar todos los tipos de eventos ==========
    @Test
    void actualizar_todosLosTiposDeEvento_deberianLlamarSmsService() {
        // Act - Probar con todos los tipos de eventos
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, "Datos 1");
        vistaPaciente.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Datos 2");
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos 3");
        vistaPaciente.actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos 4");
        vistaPaciente.actualizar(TipoEvento.NUEVO_TRIAGE, "Datos 5");

        // Assert - Debe haber enviado 5 mensajes (3 relevantes + 2 no relevantes)
        verify(smsService, times(5)).enviarMensaje(eq("Paciente"), anyString());
    }

    // ========== TEST 7: Verificar formato del mensaje para CITA_CONFIRMADA ==========
    @Test
    void actualizar_citaConfirmada_deberiaIncluirDatosEnMensaje() {
        // Arrange
        String datosEsperados = "Consultorio 3 - Dr. García";

        // Act
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, datosEsperados);

        // Assert
        verify(smsService).enviarMensaje(
                eq("Paciente"),
                eq("Su cita ha sido confirmada " + datosEsperados)
        );
    }

    // ========== TEST 8: Verificar formato del mensaje para DIAGNOSTICO_LISTO ==========
    @Test
    void actualizar_diagnosticoListo_deberiaIncluirDatosEnMensaje() {
        // Arrange
        String datosEsperados = "Puede retirar su diagnóstico en recepción";

        // Act
        vistaPaciente.actualizar(TipoEvento.DIAGNOSTICO_LISTO, datosEsperados);

        // Assert
        verify(smsService).enviarMensaje(
                eq("Paciente"),
                eq("Su diagnostico está listo " + datosEsperados)
        );
    }

    // ========== TEST 9: Verificar formato del mensaje para RESULTADO_DISPONIBLE ==========
    @Test
    void actualizar_resultadoDisponible_deberiaIncluirDatosEnMensaje() {
        // Arrange
        String datosEsperados = "Código de acceso: ABC123";

        // Act
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, datosEsperados);

        // Assert
        verify(smsService).enviarMensaje(
                eq("Paciente"),
                eq("Ya puede consultar los resultados de su exámen: " + datosEsperados)
        );
    }

    // ========== TEST 10: Verificar que se llama exactamente una vez por evento ==========
    @Test
    void actualizar_cadaEvento_debeLlamarSmsServiceUnaVez() {
        // Act
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, "Test");

        // Assert
        verify(smsService, times(1)).enviarMensaje(anyString(), anyString());
        verifyNoMoreInteractions(smsService);
    }

    // ========== TEST 11: Datos null no deberían causar error ==========
    @Test
    void actualizar_conDatosNull_noDeberiaLanzarExcepcion() {
        // Act & Assert - No debe lanzar excepción
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, null);

        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Su cita ha sido confirmada")
        );
    }

    // ========== TEST 12: Múltiples notificaciones de resultados ==========
    @Test
    void actualizar_multiplesResultadosDisponibles_deberiaEnviarTodosMensajes() {
        // Act
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Examen 1");
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Examen 2");
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Examen 3");

        // Assert
        verify(smsService, times(3)).enviarMensaje(
                eq("Paciente"),
                contains("Ya puede consultar los resultados")
        );
    }

    // ========== TEST 13: Eventos relevantes vs no relevantes ==========
    @Test
    void actualizar_eventosRelevantes_deberianEnviarMensajesEspecificos() {
        // Act - Eventos relevantes para paciente
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, "Datos 1");
        vistaPaciente.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Datos 2");
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos 3");

        // Assert - Verificar que NO se enviaron mensajes genéricos
        verify(smsService, never()).enviarMensaje(
                anyString(),
                contains("no es relevante")
        );
        verify(smsService, times(3)).enviarMensaje(eq("Paciente"), anyString());
    }

    // ========== TEST 14: Eventos no relevantes solo envían mensaje genérico ==========
    @Test
    void actualizar_eventosNoRelevantes_deberianEnviarMensajeGenerico() {
        // Act - Eventos NO relevantes para paciente
        vistaPaciente.actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos 1");
        vistaPaciente.actualizar(TipoEvento.NUEVO_TRIAGE, "Datos 2");

        // Assert - Todos deben ser mensajes genéricos
        verify(smsService, times(2)).enviarMensaje(
                eq("Paciente"),
                eq("El evento no es relevante para el paciente.")
        );
    }

    // ========== TEST 15: Flujo completo del paciente ==========
    @Test
    void actualizar_flujoCompletoPaciente_citaDiagnosticoYResultado() {
        // Act - Flujo típico: cita confirmada -> diagnóstico -> resultados
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, "Cita mañana");
        vistaPaciente.actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Gripe");
        vistaPaciente.actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Exámenes OK");

        // Assert
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Su cita ha sido confirmada")
        );
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Su diagnostico está listo")
        );
        verify(smsService, times(1)).enviarMensaje(
                eq("Paciente"),
                contains("Ya puede consultar los resultados")
        );
        verify(smsService, times(3)).enviarMensaje(anyString(), anyString());
    }

    // ========== TEST 16: Múltiples citas confirmadas ==========
    @Test
    void actualizar_multiplesCitasConfirmadas_deberiaEnviarTodosMensajes() {
        // Act
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, "Cita 1 - Cardiología");
        vistaPaciente.actualizar(TipoEvento.CITA_CONFIRMADA, "Cita 2 - Oftalmología");

        // Assert
        verify(smsService, times(2)).enviarMensaje(
                eq("Paciente"),
                contains("Su cita ha sido confirmada")
        );
    }
}