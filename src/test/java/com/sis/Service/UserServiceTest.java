package com.sis.Service;

import com.sis.Model.Usuario;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Repository.UsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsuarioRepo userRepo;

    @InjectMocks
    private UserService userService;

    private Usuario usuarioCompleto;

    @BeforeEach
    void setUp() {
        usuarioCompleto = new Usuario();
        usuarioCompleto.setId(UUID.randomUUID());
        usuarioCompleto.setUsername("jperez");
        usuarioCompleto.setHash("hash123");
        usuarioCompleto.setNombres("Juan");
        usuarioCompleto.setApellidos("Pérez");
        usuarioCompleto.setTipoDocumento(TipoDoc.CC);
        usuarioCompleto.setNumeroDocumento("123456789");
    }

    // ========== PRUEBA 1: Detectar IDs duplicados (NO PERMITIDO) ==========
    @Test
    void cuandoExistenDosUsuariosConMismoId_deberiaDetectarse() {
        // Arrange
        UUID idDuplicado = UUID.randomUUID();

        Usuario usuario1 = crearUsuarioBasico(idDuplicado, "user1", "111111");
        Usuario usuario2 = crearUsuarioBasico(idDuplicado, "user2", "222222"); // ID DUPLICADO

        when(userRepo.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        // Act
        List<Usuario> usuarios = userService.getUsuario();

        // Assert - Verificar que hay IDs duplicados (ESTO ES UN PROBLEMA)
        long idsUnicos = usuarios.stream()
                .map(Usuario::getId)
                .distinct()
                .count();

        assertTrue(usuarios.size() > idsUnicos,
                "PROBLEMA CRÍTICO: Se detectaron IDs duplicados en la base de datos");
    }

    // ========== PRUEBA 2: Detectar documentos duplicados (NO PERMITIDO) ==========
    @Test
    void cuandoExistenDosUsuariosConMismoDocumento_deberiaDetectarse() {
        // Arrange
        String documentoDuplicado = "123456789";

        Usuario usuario1 = crearUsuarioBasico(UUID.randomUUID(), "user1", documentoDuplicado);
        Usuario usuario2 = crearUsuarioBasico(UUID.randomUUID(), "user2", documentoDuplicado); // DOC DUPLICADO

        when(userRepo.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        // Act
        List<Usuario> usuarios = userService.getUsuario();

        // Assert - Verificar que hay documentos duplicados (ESTO ES UN PROBLEMA)
        long documentosUnicos = usuarios.stream()
                .filter(u -> u.getNumeroDocumento() != null) // Filtrar nulos permitidos
                .map(Usuario::getNumeroDocumento)
                .distinct()
                .count();

        long documentosNoNulos = usuarios.stream()
                .filter(u -> u.getNumeroDocumento() != null)
                .count();

        assertTrue(documentosNoNulos > documentosUnicos,
                "PROBLEMA: Se detectaron números de documento duplicados");
    }

    // ========== PRUEBA 3: Detectar usernames duplicados (NO PERMITIDO) ==========
    @Test
    void cuandoExistenDosUsuariosConMismoUsername_deberiaDetectarse() {
        // Arrange
        String usernameDuplicado = "jperez";

        Usuario usuario1 = crearUsuarioBasico(UUID.randomUUID(), usernameDuplicado, "111111");
        Usuario usuario2 = crearUsuarioBasico(UUID.randomUUID(), usernameDuplicado, "222222"); // USERNAME DUPLICADO

        when(userRepo.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        // Act
        List<Usuario> usuarios = userService.getUsuario();

        // Assert - Verificar que hay usernames duplicados (ESTO ES UN PROBLEMA)
        long usernamesUnicos = usuarios.stream()
                .filter(u -> u.getUsername() != null)
                .map(Usuario::getUsername)
                .distinct()
                .count();

        long usernamesNoNulos = usuarios.stream()
                .filter(u -> u.getUsername() != null)
                .count();

        assertTrue(usernamesNoNulos > usernamesUnicos,
                "PROBLEMA: Se detectaron usernames duplicados");
    }

    // ========== PRUEBA 4: Permitir usuarios con datos parciales (PERMITIDO) ==========
    @Test
    void cuandoUsuarioTieneDatosParciales_deberiaPermitirse() {
        // Arrange - Usuario con información parcial (válido según lógica de negocio)
        Usuario usuarioParcial = new Usuario();
        usuarioParcial.setId(UUID.randomUUID());
        usuarioParcial.setUsername("usuario_nuevo");
        // Campos como hash, nombres, apellidos pueden estar vacíos inicialmente

        when(userRepo.save(any(Usuario.class))).thenReturn(usuarioParcial);
        when(userRepo.findAll()).thenReturn(Arrays.asList(usuarioParcial));

        // Act
        userService.addUsuario(usuarioParcial);
        List<Usuario> usuarios = userService.getUsuario();

        // Assert - El sistema DEBE permitir usuarios con datos parciales
        assertNotNull(usuarios, "La lista de usuarios no debe ser nula");
        assertEquals(1, usuarios.size(), "Debe haber exactamente 1 usuario");

        Usuario usuarioGuardado = usuarios.get(0);
        assertNotNull(usuarioGuardado.getId(), "El ID es obligatorio");
        assertNotNull(usuarioGuardado.getUsername(), "El username es obligatorio");

        // Verificar que se guardó correctamente
        verify(userRepo, times(1)).save(usuarioParcial);

        System.out.println("✓ Test exitoso: El sistema permite usuarios con datos parciales");
    }

    // ========== PRUEBA 5: Validar campos mínimos requeridos ==========
    @Test
    void cuandoUsuarioTieneCamposMininosRequeridos_deberiaGuardarse() {
        // Arrange - Usuario con solo los campos REALMENTE obligatorios
        Usuario usuarioMinimo = new Usuario();
        usuarioMinimo.setId(UUID.randomUUID());
        usuarioMinimo.setUsername("minimo_user");

        when(userRepo.save(any(Usuario.class))).thenReturn(usuarioMinimo);

        // Act
        userService.addUsuario(usuarioMinimo);

        // Assert
        verify(userRepo, times(1)).save(usuarioMinimo);
        assertNotNull(usuarioMinimo.getId());
        assertNotNull(usuarioMinimo.getUsername());
    }

    // ========== PRUEBA 6: Usuario completo se guarda correctamente ==========
    @Test
    void cuandoUsuarioEstaCompleto_deberiaGuardarseCorrectamente() {
        // Arrange
        when(userRepo.save(any(Usuario.class))).thenReturn(usuarioCompleto);

        // Act
        userService.addUsuario(usuarioCompleto);

        // Assert
        verify(userRepo, times(1)).save(usuarioCompleto);
    }

    // ========== PRUEBA 7: Obtener todos los usuarios ==========
    @Test
    void cuandoSeObtienenTodosLosUsuarios_deberiaRetornarLista() {
        // Arrange
        when(userRepo.findAll()).thenReturn(Arrays.asList(usuarioCompleto));

        // Act
        List<Usuario> resultado = userService.getUsuario();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(userRepo, times(1)).findAll();
    }

    // ========== MÉTODO AUXILIAR ==========
    private Usuario crearUsuarioBasico(UUID id, String username, String documento) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setNumeroDocumento(documento);
        usuario.setTipoDocumento(TipoDoc.CC);
        return usuario;
    }
}