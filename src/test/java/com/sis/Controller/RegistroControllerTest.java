package com.sis.Controller;

import com.sis.DTO.UsuarioRegistroDTO;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Model.Enum.TipoUsuario;
import com.sis.Model.Usuario;
import com.sis.Service.UsuarioRegistroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RegistroControllerTest {

    @Mock
    private UsuarioRegistroService registroService;

    @InjectMocks
    private RegistroController registroController;

    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registroController)
                .setViewResolvers(viewResolver())
                .build();
    }

    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    // ========== TESTS DE mostrarFormularioRegistro() - GET /registro ==========

    @Test
    void mostrarFormularioRegistro_DebeRetornarVistaRegistro() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("tiposDocumento"))
                .andExpect(model().attributeExists("tiposUsuario"));
    }

    @Test
    void mostrarFormularioRegistro_DebeAgregarUsuarioDTOVacio() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("usuario", instanceOf(UsuarioRegistroDTO.class)));
    }

    @Test
    void mostrarFormularioRegistro_DebeAgregarTodosLosTiposDocumento() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("tiposDocumento", TipoDoc.values()));
    }

    @Test
    void mostrarFormularioRegistro_DebeAgregarTodosLosTiposUsuario() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("tiposUsuario", TipoUsuario.values()));
    }

    @Test
    void mostrarFormularioRegistro_DebeRetornarStatus200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk());
    }

    // ========== TESTS DE registrarUsuario() - POST /registro ==========

    /*
    @Test
    void registrarUsuario_ConDatosValidos_DebeRedirigirALogin() throws Exception {

        // Arrange
        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(new HashMap<>());
        when(registroService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                .thenReturn(new Usuario());

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "juan@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("mensaje"))
                .andExpect(flash().attribute("mensaje",
                        "Usuario registrado exitosamente. Ahora puedes iniciar sesión."));

        verify(registroService, times(1)).validarRegistro(any(UsuarioRegistroDTO.class));
        verify(registroService, times(1)).registrarUsuario(any(UsuarioRegistroDTO.class));
    }

     */
    @Test
    void registrarUsuario_ConErroresDeValidacion_DebeRetornarFormulario() throws Exception {
        // Act & Assert - enviando datos incompletos para forzar errores de validación
        mockMvc.perform(post("/registro")
                        .param("username", "")) // Campo vacío causa error de validación
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("tiposDocumento"))
                .andExpect(model().attributeExists("tiposUsuario"));

        // No debe llamar al servicio si hay errores de validación
        verify(registroService, never()).validarRegistro(any());
        verify(registroService, never()).registrarUsuario(any());
    }

    /*
    @Test
    void registrarUsuario_ConErroresDeNegocio_DebeRetornarFormularioConErrores() throws Exception {
        // Arrange
        Map<String, String> errores = new HashMap<>();
        errores.put("username", "El nombre de usuario ya existe");
        errores.put("email", "El email ya está registrado");

        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(errores);

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "existinguser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "existing@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("usernameError"))
                .andExpect(model().attribute("usernameError", "El nombre de usuario ya existe"))
                .andExpect(model().attributeExists("emailError"))
                .andExpect(model().attribute("emailError", "El email ya está registrado"))
                .andExpect(model().attributeExists("tiposDocumento"))
                .andExpect(model().attributeExists("tiposUsuario"));

        verify(registroService, times(1)).validarRegistro(any(UsuarioRegistroDTO.class));
        verify(registroService, never()).registrarUsuario(any());
    }

     */
    /*
    @Test
    void registrarUsuario_ConPasswordsNoCoinciden_DebeRetornarFormularioConError() throws Exception {
        // Arrange
        Map<String, String> errores = new HashMap<>();
        errores.put("password", "Las contraseñas no coinciden");

        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(errores);

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "juan@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("passwordError"))
                .andExpect(model().attribute("passwordError", "Las contraseñas no coinciden"));

        verify(registroService, times(1)).validarRegistro(any(UsuarioRegistroDTO.class));
        verify(registroService, never()).registrarUsuario(any());
    }

     */

    /*
    @Test
    void registrarUsuario_ConExcepcionEnServicio_DebeRetornarFormularioConError() throws Exception {
        // Arrange
        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(new HashMap<>());
        when(registroService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "juan@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error",
                        "Ocurrió un error al registrar el usuario. Por favor, intenta nuevamente."))
                .andExpect(model().attributeExists("tiposDocumento"))
                .andExpect(model().attributeExists("tiposUsuario"));

        verify(registroService, times(1)).validarRegistro(any(UsuarioRegistroDTO.class));
        verify(registroService, times(1)).registrarUsuario(any(UsuarioRegistroDTO.class));
    }
     */

    /*
    @Test
    void registrarUsuario_ConUsernameYaExistente_DebeRetornarFormularioConError() throws Exception {
        // Arrange
        Map<String, String> errores = new HashMap<>();
        errores.put("username", "El nombre de usuario ya existe");

        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(errores);


        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "existinguser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "new@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("usernameError"));

        verify(registroService, never()).registrarUsuario(any());
    }

    @Test
    void registrarUsuario_ConDocumentoYaRegistrado_DebeRetornarFormularioConError() throws Exception {
        // Arrange
        Map<String, String> errores = new HashMap<>();
        errores.put("numeroDocumento", "El número de documento ya está registrado");

        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(errores);

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "newuser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "new@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("numeroDocumentoError"));

        verify(registroService, never()).registrarUsuario(any());
    }

     */
    /*
    @Test
    void registrarUsuario_DebeLlamarServicioConDatosCorrectos() throws Exception {
        // Arrange
        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(new HashMap<>());
        when(registroService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                .thenReturn(new Usuario());

        // Act
        mockMvc.perform(post("/registro")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "juan@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().is3xxRedirection());

        // Assert
        verify(registroService, times(1)).validarRegistro(any(UsuarioRegistroDTO.class));
        verify(registroService, times(1)).registrarUsuario(any(UsuarioRegistroDTO.class));
    }

     */

    /*
    @Test
    void registrarUsuario_ConDiferentesTiposDocumento_DebeRegistrarCorrectamente() throws Exception {
        // Arrange
        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(new HashMap<>());
        when(registroService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                .thenReturn(new Usuario());

        // Test con cada tipo de documento
        for (TipoDoc tipo : TipoDoc.values()) {
            // Act & Assert
            mockMvc.perform(post("/registro")
                            .param("username", "testuser")
                            .param("password", "password123")
                            .param("confirmPassword", "password123")
                            .param("nombres", "Juan")
                            .param("apellidos", "Pérez")
                            .param("email", "juan@test.com")
                            .param("tipoDocumento", tipo.name())
                            .param("numeroDocumento", "123456789")
                            .param("direccion", "Calle 123")
                            .param("tipoUsuario", "CLIENTE"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"));
        }
    }

     */

    @Test
    void registrarUsuario_ConDiferentesTiposUsuario_DebeRegistrarCorrectamente() throws Exception {
        // Arrange
        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(new HashMap<>());
        when(registroService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                .thenReturn(new Usuario());

        // Test con cada tipo de usuario
        for (TipoUsuario tipo : TipoUsuario.values()) {
            // Act & Assert
            mockMvc.perform(post("/registro")
                            .param("username", "testuser")
                            .param("password", "password123")
                            .param("confirmPassword", "password123")
                            .param("nombres", "Juan")
                            .param("apellidos", "Pérez")
                            .param("email", "juan@test.com")
                            .param("tipoDocumento", "CC")
                            .param("numeroDocumento", "123456789")
                            .param("direccion", "Calle 123")
                            .param("tipoUsuario", tipo.name()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"));
        }
    }

    // ========== TESTS DE FLUJOS COMPLETOS ==========

    /*
    @Test
    void flujoRegistroCompleto_UsuarioNuevo_DebeRedirigirALogin() throws Exception {
        // Arrange
        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(new HashMap<>());
        when(registroService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                .thenReturn(new Usuario());

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "newuser")
                        .param("password", "securePass123")
                        .param("confirmPassword", "securePass123")
                        .param("nombres", "María")
                        .param("apellidos", "González")
                        .param("email", "maria@example.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "987654321")
                        .param("direccion", "Avenida 45")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("mensaje",
                        "Usuario registrado exitosamente. Ahora puedes iniciar sesión."));
    }
     */

    /*
    @Test
    void flujoRegistroFallido_DatosInvalidos_DebeMostrarFormularioConErrores() throws Exception {
        // Arrange
        Map<String, String> errores = new HashMap<>();
        errores.put("username", "El nombre de usuario ya existe");
        errores.put("email", "El email ya está registrado");

        when(registroService.validarRegistro(any(UsuarioRegistroDTO.class)))
                .thenReturn(errores);

        // Act & Assert
        mockMvc.perform(post("/registro")
                        .param("username", "existinguser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("nombres", "Juan")
                        .param("apellidos", "Pérez")
                        .param("email", "existing@test.com")
                        .param("tipoDocumento", "CC")
                        .param("numeroDocumento", "123456789")
                        .param("direccion", "Calle 123")
                        .param("tipoUsuario", "CLIENTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("usernameError"))
                .andExpect(model().attributeExists("emailError"));
    }

     */
}