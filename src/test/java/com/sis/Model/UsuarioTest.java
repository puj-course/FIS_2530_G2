package com.sis.Model;

import com.sis.Model.Enum.TipoDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    // ========== TEST DE GETTERS Y SETTERS ==========

    @Test
    void getId() {
        UUID id = UUID.randomUUID();
        usuario.setId(id);
        assertEquals(id, usuario.getId());
    }

    @Test
    void setId() {
        UUID id = UUID.randomUUID();
        usuario.setId(id);
        assertNotNull(usuario.getId());
        assertEquals(id, usuario.getId());
    }

    @Test
    void getUsername() {
        usuario.setUsername("jperez");
        assertEquals("jperez", usuario.getUsername());
    }

    @Test
    void setUsername() {
        usuario.setUsername("testuser");
        assertEquals("testuser", usuario.getUsername());
    }

    @Test
    void getHash() {
        usuario.setHash("hash123");
        assertEquals("hash123", usuario.getHash());
    }

    @Test
    void setHash() {
        usuario.setHash("$2a$10$abc123");
        assertEquals("$2a$10$abc123", usuario.getHash());
    }

    @Test
    void getNombres() {
        usuario.setNombres("Juan");
        assertEquals("Juan", usuario.getNombres());
    }

    @Test
    void setNombres() {
        usuario.setNombres("Pedro");
        assertEquals("Pedro", usuario.getNombres());
    }

    @Test
    void getApellidos() {
        usuario.setApellidos("Pérez");
        assertEquals("Pérez", usuario.getApellidos());
    }

    @Test
    void setApellidos() {
        usuario.setApellidos("García");
        assertEquals("García", usuario.getApellidos());
    }

    @Test
    void getNombreCompleto() {
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        assertEquals("Juan Pérez", usuario.getNombreCompleto());
    }

    @Test
    void getEmail() {
        usuario.setEmail("test@email.com");
        assertEquals("test@email.com", usuario.getEmail());
    }

    @Test
    void setEmail() {
        usuario.setEmail("usuario@hospital.com");
        assertEquals("usuario@hospital.com", usuario.getEmail());
    }

    @Test
    void getTipoDocumento() {
        usuario.setTipoDocumento(TipoDoc.CC);
        assertEquals(TipoDoc.CC, usuario.getTipoDocumento());
    }

    @Test
    void setTipoDocumento() {
        usuario.setTipoDocumento(TipoDoc.PASAPORTE);
        assertEquals(TipoDoc.PASAPORTE, usuario.getTipoDocumento());
    }

    @Test
    void getNumeroDocumento() {
        usuario.setNumeroDocumento("123456789");
        assertEquals("123456789", usuario.getNumeroDocumento());
    }

    @Test
    void setNumeroDocumento() {
        usuario.setNumeroDocumento("987654321");
        assertEquals("987654321", usuario.getNumeroDocumento());
    }

    @Test
    void getDireccion() {
        usuario.setDireccion("Calle 123 #45-67");
        assertEquals("Calle 123 #45-67", usuario.getDireccion());
    }

    @Test
    void setDireccion() {
        usuario.setDireccion("Avenida 50 #30-20");
        assertEquals("Avenida 50 #30-20", usuario.getDireccion());
    }

    @Test
    void isActivo() {
        usuario.setActivo(true);
        assertTrue(usuario.isActivo());
    }

    @Test
    void setActivo() {
        usuario.setActivo(false);
        assertFalse(usuario.isActivo());
    }

    @Test
    void getCreadoEn() {
        LocalDateTime ahora = LocalDateTime.now();
        usuario.setCreadoEn(ahora);
        assertEquals(ahora, usuario.getCreadoEn());
    }

    @Test
    void setCreadoEn() {
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 15, 10, 30);
        usuario.setCreadoEn(fecha);
        assertEquals(fecha, usuario.getCreadoEn());
    }

    // ========== TEST DEL MÉTODO @PrePersist ==========

    @Test
    void onCreate() {
        // Simular que se va a persistir el usuario
        usuario.onCreate();
        assertNotNull(usuario.getCreadoEn(), "La fecha de creación debe establecerse automáticamente");
    }

    @Test
    void onCreateNoSobrescribeFechaExistente() {
        LocalDateTime fechaOriginal = LocalDateTime.of(2023, 12, 1, 10, 0);
        usuario.setCreadoEn(fechaOriginal);

        // Llamar onCreate no debe cambiar la fecha si ya existe
        usuario.onCreate();

        assertEquals(fechaOriginal, usuario.getCreadoEn(),
                "La fecha de creación no debe cambiar si ya estaba establecida");
    }

    // ========== TESTS ADICIONALES DE VALIDACIÓN ==========

    @Test
    void usuarioNuevoDeberiaEstarActivoPorDefecto() {
        Usuario nuevoUsuario = new Usuario();
        assertTrue(nuevoUsuario.isActivo(), "Un usuario nuevo debe estar activo por defecto");
    }

    @Test
    void usuarioCompletoDeberiaTenerTodosLosCampos() {
        UUID id = UUID.randomUUID();
        usuario.setId(id);
        usuario.setUsername("jperez");
        usuario.setHash("hash123");
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        usuario.setEmail("juan@email.com");
        usuario.setTipoDocumento(TipoDoc.CC);
        usuario.setNumeroDocumento("123456789");
        usuario.setDireccion("Calle 123");
        usuario.setActivo(true);
        usuario.setCreadoEn(LocalDateTime.now());

        assertAll("Verificar usuario completo",
                () -> assertEquals(id, usuario.getId()),
                () -> assertEquals("jperez", usuario.getUsername()),
                () -> assertEquals("hash123", usuario.getHash()),
                () -> assertEquals("Juan", usuario.getNombres()),
                () -> assertEquals("Pérez", usuario.getApellidos()),
                () -> assertEquals("juan@email.com", usuario.getEmail()),
                () -> assertEquals(TipoDoc.CC, usuario.getTipoDocumento()),
                () -> assertEquals("123456789", usuario.getNumeroDocumento()),
                () -> assertEquals("Calle 123", usuario.getDireccion()),
                () -> assertTrue(usuario.isActivo()),
                () -> assertNotNull(usuario.getCreadoEn())
        );
    }

    @Test
    void usuarioConCamposNulosDeberiaSerValido() {
        // Según tu lógica de negocio, se permiten campos nulos
        usuario.setId(UUID.randomUUID());
        usuario.setUsername("usuario_parcial");
        // Dejamos email, direccion, etc como null

        assertNotNull(usuario.getId());
        assertNotNull(usuario.getUsername());
        assertNull(usuario.getEmail(), "Email puede ser nulo");
        assertNull(usuario.getDireccion(), "Dirección puede ser nula");
    }

    @Test
    void getNombreCompletoConNombresNulos() {
        usuario.setNombres(null);
        usuario.setApellidos(null);

        String nombreCompleto = usuario.getNombreCompleto();
        assertEquals("null null", nombreCompleto,
                "getNombreCompleto debe manejar valores nulos");
    }

    @Test
    void cambiarEstadoActivoAInactivo() {
        usuario.setActivo(true);
        assertTrue(usuario.isActivo());

        usuario.setActivo(false);
        assertFalse(usuario.isActivo());
    }

    @Test
    void todosLosTiposDeDocumentoSonValidos() {
        assertAll("Verificar todos los tipos de documento",
                () -> {
                    usuario.setTipoDocumento(TipoDoc.CC);
                    assertEquals(TipoDoc.CC, usuario.getTipoDocumento());
                },
                () -> {
                    usuario.setTipoDocumento(TipoDoc.TI);
                    assertEquals(TipoDoc.TI, usuario.getTipoDocumento());
                },
                () -> {
                    usuario.setTipoDocumento(TipoDoc.CE);
                    assertEquals(TipoDoc.CE, usuario.getTipoDocumento());
                },
                () -> {
                    usuario.setTipoDocumento(TipoDoc.PASAPORTE);
                    assertEquals(TipoDoc.PASAPORTE, usuario.getTipoDocumento());
                }
        );
    }

    @Test
    void constructorPorDefectoDeberiaCrearUsuarioVacio() {
        Usuario nuevoUsuario = new Usuario();

        assertNull(nuevoUsuario.getId());
        assertNull(nuevoUsuario.getUsername());
        assertNull(nuevoUsuario.getHash());
        assertNull(nuevoUsuario.getNombres());
        assertNull(nuevoUsuario.getApellidos());
        assertNull(nuevoUsuario.getEmail());
        assertNull(nuevoUsuario.getTipoDocumento());
        assertNull(nuevoUsuario.getNumeroDocumento());
        assertNull(nuevoUsuario.getDireccion());
        assertTrue(nuevoUsuario.isActivo(), "Debe estar activo por defecto");
        assertNull(nuevoUsuario.getCreadoEn());
    }
}