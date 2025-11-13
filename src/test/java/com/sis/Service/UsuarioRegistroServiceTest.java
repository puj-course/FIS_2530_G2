package com.sis.Service;

import com.sis.DTO.UsuarioRegistroDTO;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Model.Enum.TipoUsuario;
import com.sis.Model.Usuario;
import com.sis.Repository.UsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRegistroServiceTest {

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioRegistroService usuarioRegistroService;

    private UsuarioRegistroDTO dtoValido;

    @BeforeEach
    void setUp() {
        dtoValido = new UsuarioRegistroDTO();
        dtoValido.setUsername("testuser");
        dtoValido.setPassword("password123");
        dtoValido.setConfirmPassword("password123");
        dtoValido.setNombres("Juan");
        dtoValido.setApellidos("Pérez");
        dtoValido.setEmail("juan@example.com");
        dtoValido.setTipoDocumento(TipoDoc.CC);
        dtoValido.setNumeroDocumento("123456789");
        dtoValido.setDireccion("Calle 123");
        dtoValido.setTipoUsuario(TipoUsuario.PACIENTE);
    }

    // ========== TESTS DE validarRegistro() ==========

    @Test
    void validarRegistro_CuandoDatosSonValidos_DebeRetornarMapaVacio() {
        // Arrange
        when(usuarioRepo.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepo.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(usuarioRepo.existsByEmail(anyString())).thenReturn(false);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertTrue(errores.isEmpty());
        verify(usuarioRepo).existsByUsername("testuser");
        verify(usuarioRepo).existsByNumeroDocumento("123456789");
        verify(usuarioRepo).existsByEmail("juan@example.com");
    }

    @Test
    void validarRegistro_CuandoPasswordsNoCoinciden_DebeRetornarError() {
        // Arrange
        dtoValido.setConfirmPassword("differentPassword");
        when(usuarioRepo.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepo.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(usuarioRepo.existsByEmail(anyString())).thenReturn(false);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertFalse(errores.isEmpty());
        assertEquals("Las contraseñas no coinciden", errores.get("password"));
    }

    @Test
    void validarRegistro_CuandoUsernameYaExiste_DebeRetornarError() {
        // Arrange
        when(usuarioRepo.existsByUsername("testuser")).thenReturn(true);
        when(usuarioRepo.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(usuarioRepo.existsByEmail(anyString())).thenReturn(false);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertFalse(errores.isEmpty());
        assertEquals("El nombre de usuario ya existe", errores.get("username"));
        verify(usuarioRepo).existsByUsername("testuser");
    }

    @Test
    void validarRegistro_CuandoDocumentoYaExiste_DebeRetornarError() {
        // Arrange
        when(usuarioRepo.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepo.existsByNumeroDocumento("123456789")).thenReturn(true);
        when(usuarioRepo.existsByEmail(anyString())).thenReturn(false);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertFalse(errores.isEmpty());
        assertEquals("El número de documento ya está registrado", errores.get("numeroDocumento"));
        verify(usuarioRepo).existsByNumeroDocumento("123456789");
    }

    @Test
    void validarRegistro_CuandoEmailYaExiste_DebeRetornarError() {
        // Arrange
        when(usuarioRepo.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepo.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(usuarioRepo.existsByEmail("juan@example.com")).thenReturn(true);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertFalse(errores.isEmpty());
        assertEquals("El email ya está registrado", errores.get("email"));
        verify(usuarioRepo).existsByEmail("juan@example.com");
    }

    @Test
    void validarRegistro_CuandoEmailEsNull_NoDebeValidarEmail() {
        // Arrange
        dtoValido.setEmail(null);
        when(usuarioRepo.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepo.existsByNumeroDocumento(anyString())).thenReturn(false);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertTrue(errores.isEmpty());
        verify(usuarioRepo, never()).existsByEmail(anyString());
    }

    @Test
    void validarRegistro_CuandoEmailEsVacio_NoDebeValidarEmail() {
        // Arrange
        dtoValido.setEmail("");
        when(usuarioRepo.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepo.existsByNumeroDocumento(anyString())).thenReturn(false);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertTrue(errores.isEmpty());
        verify(usuarioRepo, never()).existsByEmail(anyString());
    }

    @Test
    void validarRegistro_CuandoMultiplesErrores_DebeRetornarTodosLosErrores() {
        // Arrange
        dtoValido.setConfirmPassword("differentPassword");
        when(usuarioRepo.existsByUsername("testuser")).thenReturn(true);
        when(usuarioRepo.existsByNumeroDocumento("123456789")).thenReturn(true);
        when(usuarioRepo.existsByEmail("juan@example.com")).thenReturn(true);

        // Act
        Map<String, String> errores = usuarioRegistroService.validarRegistro(dtoValido);

        // Assert
        assertEquals(4, errores.size());
        assertTrue(errores.containsKey("password"));
        assertTrue(errores.containsKey("username"));
        assertTrue(errores.containsKey("numeroDocumento"));
        assertTrue(errores.containsKey("email"));
    }

    // ========== TESTS DE registrarUsuario() ==========

    @Test
    void registrarUsuario_CuandoDatosSonValidos_DebeGuardarUsuarioCorrectamente() {
        // Arrange
        String hashedPassword = "$2a$10$hashedPassword";
        when(passwordEncoder.encode("password123")).thenReturn(hashedPassword);
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioRegistroService.registrarUsuario(dtoValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        assertEquals("Juan", resultado.getNombres());
        assertEquals("Pérez", resultado.getApellidos());
        assertEquals("juan@example.com", resultado.getEmail());
        assertEquals(TipoDoc.CC, resultado.getTipoDocumento());
        assertEquals("123456789", resultado.getNumeroDocumento());
        assertEquals("Calle 123", resultado.getDireccion());
        assertEquals(TipoUsuario.PACIENTE, resultado.getTipoUsuario());
        assertEquals(hashedPassword, resultado.getHash());
        assertTrue(resultado.isActivo());

        verify(passwordEncoder).encode("password123");
        verify(usuarioRepo).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DebeHashearPassword() {
        // Arrange
        String hashedPassword = "$2a$10$superHashedPassword";
        when(passwordEncoder.encode("password123")).thenReturn(hashedPassword);
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioRegistroService.registrarUsuario(dtoValido);

        // Assert
        assertEquals(hashedPassword, resultado.getHash());
        assertNotEquals("password123", resultado.getHash());
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registrarUsuario_DebeEstablecerUsuarioComoActivo() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioRegistroService.registrarUsuario(dtoValido);

        // Assert
        assertTrue(resultado.isActivo());
    }

    @Test
    void registrarUsuario_DebeLlamarSaveDelRepositorio() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        usuarioRegistroService.registrarUsuario(dtoValido);

        // Assert
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepo).save(usuarioCaptor.capture());

        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertEquals("testuser", usuarioGuardado.getUsername());
        assertEquals("Juan", usuarioGuardado.getNombres());
    }

    @Test
    void registrarUsuario_CuandoEmailEsNull_DebeGuardarConEmailNull() {
        // Arrange
        dtoValido.setEmail(null);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioRegistroService.registrarUsuario(dtoValido);

        // Assert
        assertNull(resultado.getEmail());
    }

    @Test
    void registrarUsuario_DebeRetornarUsuarioGuardado() {
        // Arrange
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setUsername("testuser");
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hash");
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Act
        Usuario resultado = usuarioRegistroService.registrarUsuario(dtoValido);

        // Assert
        assertSame(usuarioGuardado, resultado);
    }

    // ========== TESTS DE usernameExiste() ==========

    @Test
    void usernameExiste_CuandoUsernameExiste_DebeRetornarTrue() {
        // Arrange
        when(usuarioRepo.existsByUsername("testuser")).thenReturn(true);

        // Act
        boolean resultado = usuarioRegistroService.usernameExiste("testuser");

        // Assert
        assertTrue(resultado);
        verify(usuarioRepo).existsByUsername("testuser");
    }

    @Test
    void usernameExiste_CuandoUsernameNoExiste_DebeRetornarFalse() {
        // Arrange
        when(usuarioRepo.existsByUsername("nonexistent")).thenReturn(false);

        // Act
        boolean resultado = usuarioRegistroService.usernameExiste("nonexistent");

        // Assert
        assertFalse(resultado);
        verify(usuarioRepo).existsByUsername("nonexistent");
    }

    // ========== TESTS DE documentoExiste() ==========

    @Test
    void documentoExiste_CuandoDocumentoExiste_DebeRetornarTrue() {
        // Arrange
        when(usuarioRepo.existsByNumeroDocumento("123456789")).thenReturn(true);

        // Act
        boolean resultado = usuarioRegistroService.documentoExiste("123456789");

        // Assert
        assertTrue(resultado);
        verify(usuarioRepo).existsByNumeroDocumento("123456789");
    }

    @Test
    void documentoExiste_CuandoDocumentoNoExiste_DebeRetornarFalse() {
        // Arrange
        when(usuarioRepo.existsByNumeroDocumento("999999999")).thenReturn(false);

        // Act
        boolean resultado = usuarioRegistroService.documentoExiste("999999999");

        // Assert
        assertFalse(resultado);
        verify(usuarioRepo).existsByNumeroDocumento("999999999");
    }
}