package com.sis.Service;

import com.sis.Interface.IObservador;
import com.sis.Model.Enum.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventBusTest {

    private EventBus eventBus;

    @Mock
    private IObservador observador1;

    @Mock
    private IObservador observador2;

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
    }

    // ========== TESTS DE SUSCRIBIR ==========

    @Test
    void suscribir() {
        // Act
        eventBus.suscribir(observador1);
        eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "Datos de prueba");

        // Assert
        verify(observador1, times(1)).actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos de prueba");
    }

    @Test
    void suscribirMultiplesObservadores() {
        // Act
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador2);
        eventBus.publicar(TipoEvento.NUEVO_TRIAGE, "Triage urgente");

        // Assert
        verify(observador1, times(1)).actualizar(TipoEvento.NUEVO_TRIAGE, "Triage urgente");
        verify(observador2, times(1)).actualizar(TipoEvento.NUEVO_TRIAGE, "Triage urgente");
    }

    @Test
    void suscribirMismoObservadorDosveces_noDeberiaAgregarDuplicados() {
        // Act
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador1); // Intentar suscribir de nuevo
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, "Resultado");

        // Assert - Solo debe notificarse UNA vez
        verify(observador1, times(1)).actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Resultado");
    }

    // ========== TESTS DE DESUSCRIBIR ==========

    @Test
    void desuscribir() {
        // Arrange
        eventBus.suscribir(observador1);

        // Act
        eventBus.desuscribir(observador1);
        eventBus.publicar(TipoEvento.CITA_CONFIRMADA, "Datos");

        // Assert - No debe recibir notificaciones
        verify(observador1, never()).actualizar(any(), any());
    }

    @Test
    void desuscribirUnObservadorNoAfectaOtros() {
        // Arrange
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador2);

        // Act
        eventBus.desuscribir(observador1);
        eventBus.publicar(TipoEvento.RESULTADO_DISPONIBLE, "Lab results");

        // Assert
        verify(observador1, never()).actualizar(any(), any());
        verify(observador2, times(1)).actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Lab results");
    }

    @Test
    void desuscribirObservadorNoSuscrito_noDeberiaLanzarError() {
        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> eventBus.desuscribir(observador1));
    }

    // ========== TESTS DE PUBLICAR ==========

    @Test
    void publicar() {
        // Arrange
        eventBus.suscribir(observador1);
        String datos = "Información importante";

        // Act
        eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, datos);

        // Assert
        verify(observador1, times(1)).actualizar(TipoEvento.PACIENTE_LLEGADA, datos);
    }

    @Test
    void publicarSinObservadores_noDeberiaLanzarError() {
        // Act & Assert - No debe fallar aunque no haya observadores
        assertDoesNotThrow(() ->
                eventBus.publicar(TipoEvento.NUEVO_TRIAGE, "Datos")
        );
    }

    @Test
    void publicarNotificaTodosLosObservadores() {
        // Arrange
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador2);

        // Act
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, "Diagnóstico");

        // Assert
        verify(observador1, times(1)).actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Diagnóstico");
        verify(observador2, times(1)).actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Diagnóstico");
    }

    @Test
    void publicarConDiferentesTiposDeEventos() {
        // Arrange
        eventBus.suscribir(observador1);

        // Act
        eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "Evento 1");
        eventBus.publicar(TipoEvento.NUEVO_TRIAGE, "Evento 2");
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, "Evento 3");

        // Assert
        verify(observador1, times(1)).actualizar(TipoEvento.PACIENTE_LLEGADA, "Evento 1");
        verify(observador1, times(1)).actualizar(TipoEvento.NUEVO_TRIAGE, "Evento 2");
        verify(observador1, times(1)).actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Evento 3");
    }

    @Test
    void publicarConDatosNull() {
        // Arrange
        eventBus.suscribir(observador1);

        // Act
        eventBus.publicar(TipoEvento.CITA_CONFIRMADA, null);

        // Assert
        verify(observador1, times(1)).actualizar(TipoEvento.CITA_CONFIRMADA, null);
    }

    @Test
    void publicarConDiferentesTiposDeDatos() {
        // Arrange
        eventBus.suscribir(observador1);
        ArgumentCaptor<Object> datosCaptor = ArgumentCaptor.forClass(Object.class);

        // Act - Publicar con diferentes tipos de datos
        eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "String");
        eventBus.publicar(TipoEvento.NUEVO_TRIAGE, 123);
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, true);

        // Assert
        verify(observador1, times(3)).actualizar(any(TipoEvento.class), datosCaptor.capture());

        assertEquals(3, datosCaptor.getAllValues().size());
        assertEquals("String", datosCaptor.getAllValues().get(0));
        assertEquals(123, datosCaptor.getAllValues().get(1));
        assertEquals(true, datosCaptor.getAllValues().get(2));
    }

    // ========== TESTS DE MANEJO DE ERRORES ==========

    @Test
    void publicarCuandoObservadorLanzaExcepcion_deberiaNotificarOtrosObservadores() {
        // Arrange
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador2);

        // Simular que observador1 lanza excepción
        doThrow(new RuntimeException("Error en observador1"))
                .when(observador1).actualizar(any(), any());

        // Act
        eventBus.publicar(TipoEvento.RESULTADO_DISPONIBLE, "Datos");

        // Assert - observador2 debe recibir notificación aunque observador1 falle
        verify(observador1, times(1)).actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos");
        verify(observador2, times(1)).actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos");
    }

    @Test
    void publicarCuandoTodosLosObservadoresLanzanExcepcion_noDeberiaLanzarError() {
        // Arrange
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador2);

        doThrow(new RuntimeException("Error 1")).when(observador1).actualizar(any(), any());
        doThrow(new RuntimeException("Error 2")).when(observador2).actualizar(any(), any());

        // Act & Assert - No debe propagar la excepción
        assertDoesNotThrow(() ->
                eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "Datos")
        );
    }

    // ========== TESTS DE TODOS LOS TIPOS DE EVENTOS ==========

    @Test
    void todosLosTiposDeEventosFuncionan() {
        // Arrange
        eventBus.suscribir(observador1);

        // Act & Assert
        assertAll("Verificar todos los tipos de eventos",
                () -> {
                    eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "Datos");
                    verify(observador1, times(1)).actualizar(TipoEvento.PACIENTE_LLEGADA, "Datos");
                },
                () -> {
                    eventBus.publicar(TipoEvento.NUEVO_TRIAGE, "Datos");
                    verify(observador1, times(1)).actualizar(TipoEvento.NUEVO_TRIAGE, "Datos");
                },
                () -> {
                    eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, "Datos");
                    verify(observador1, times(1)).actualizar(TipoEvento.DIAGNOSTICO_LISTO, "Datos");
                },
                () -> {
                    eventBus.publicar(TipoEvento.CITA_CONFIRMADA, "Datos");
                    verify(observador1, times(1)).actualizar(TipoEvento.CITA_CONFIRMADA, "Datos");
                },
                () -> {
                    eventBus.publicar(TipoEvento.RESULTADO_DISPONIBLE, "Datos");
                    verify(observador1, times(1)).actualizar(TipoEvento.RESULTADO_DISPONIBLE, "Datos");
                }
        );
    }

    // ========== TEST DE INTEGRACIÓN COMPLETO ==========

    @Test
    void flujoCompletoSuscribirPublicarDesuscribir() {
        // 1. Suscribir observadores
        eventBus.suscribir(observador1);
        eventBus.suscribir(observador2);

        // 2. Publicar evento - ambos deben recibirlo
        eventBus.publicar(TipoEvento.PACIENTE_LLEGADA, "Paciente llegó");
        verify(observador1, times(1)).actualizar(TipoEvento.PACIENTE_LLEGADA, "Paciente llegó");
        verify(observador2, times(1)).actualizar(TipoEvento.PACIENTE_LLEGADA, "Paciente llegó");

        // 3. Desuscribir observador1
        eventBus.desuscribir(observador1);

        // 4. Publicar otro evento - solo observador2 debe recibirlo
        eventBus.publicar(TipoEvento.NUEVO_TRIAGE, "Triage realizado");
        verify(observador1, times(1)).actualizar(any(), any()); // Solo 1 vez (anterior)
        verify(observador2, times(2)).actualizar(any(), any()); // 2 veces

        // 5. Desuscribir observador2
        eventBus.desuscribir(observador2);

        // 6. Publicar sin observadores
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO, "Diagnóstico");
        verify(observador1, times(1)).actualizar(any(), any()); // Sigue en 1
        verify(observador2, times(2)).actualizar(any(), any()); // Sigue en 2
    }
}