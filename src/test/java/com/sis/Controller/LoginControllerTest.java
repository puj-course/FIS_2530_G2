package com.sis.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // ⭐ Configurar MockMvc con un ViewResolver para evitar errores de "Circular view path"
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers(viewResolver())
                .build();
    }

    /**
     * Configura un ViewResolver simple para los tests
     * Esto evita el error de "Circular view path"
     */
    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    // ========== TESTS DE login() ==========

    @Test
    void login_SinParametros_DebeRetornarVistaLogin() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    void login_ConParametroError_DebeAgregarMensajeError() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Usuario o contraseña incorrectos"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    void login_ConParametroLogout_DebeAgregarMensajeLogout() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login").param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Sesión cerrada correctamente"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void login_ConAmbosParametros_DebeAgregarAmbosMensajes() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login")
                        .param("error", "")
                        .param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("error", "Usuario o contraseña incorrectos"))
                .andExpect(model().attribute("message", "Sesión cerrada correctamente"));
    }

    @Test
    void login_ConParametroErrorConValor_DebeAgregarMensajeError() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Usuario o contraseña incorrectos"));
    }

    @Test
    void login_ConParametroLogoutConValor_DebeAgregarMensajeLogout() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Sesión cerrada correctamente"));
    }

    @Test
    void login_ConParametroErrorNull_NoDebeAgregarMensajeError() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void login_ConParametroLogoutNull_NoDebeAgregarMensajeLogout() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    void login_DebeRetornarStatusOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void login_DebeRetornarVistaLoginCorrectamente() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    // ========== TESTS DE dashboard() ==========

    @Test
    void dashboard_DebeRetornarVistaDashboard() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void dashboard_DebeRetornarStatusOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    void dashboard_NoDebeAgregarAtributosAlModelo() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    // ========== TESTS DE INTEGRACIÓN / FLUJOS COMPLETOS ==========

    @Test
    void flujoLoginFallido_DebeRedirigirConError() throws Exception {
        // Simula un login fallido que redirigiría a /login?error
        mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Usuario o contraseña incorrectos"));
    }

    @Test
    void flujoLogoutExitoso_DebeRedirigirConMensaje() throws Exception {
        // Simula un logout exitoso que redirigiría a /login?logout
        mockMvc.perform(get("/login").param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "Sesión cerrada correctamente"));
    }

    @Test
    void flujoAccesoDashboard_DebeMostrarVista() throws Exception {
        // Simula acceso al dashboard después de login exitoso
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }
}