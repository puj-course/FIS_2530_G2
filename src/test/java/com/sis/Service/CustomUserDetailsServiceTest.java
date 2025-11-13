package com.sis.Service;

import com.sis.Model.Usuario;
import com.sis.Repository.UsuarioRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepo usuarioRepo;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Usuario usuarioActivo;
    private Usuario usuarioInactivo;

    @BeforeEach
    void setUp() {
        // Usuario activo para las pruebas
        usuarioActivo = new Usuario();
        usuarioActivo.setUsername("testuser");
        usuarioActivo.setHash("$2a$10$hashedPassword");
        usuarioActivo.setActivo(true);

        // Usuario inactivo para las pruebas
        usuarioInactivo = new Usuario();
        usuarioInactivo.setUsername("inactiveuser");
        usuarioInactivo.setHash("$2a$10$hashedPassword");
        usuarioInactivo.setActivo(false);
    }

    @Test
    void loadUserByUsername_CuandoUsuarioExisteYEstaActivo_DebeRetornarUserDetails() {
        // Arrange
        String username = "testuser";
        when(usuarioRepo.findByUsername(username)).thenReturn(Optional.of(usuarioActivo));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("$2a$10$hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(usuarioRepo, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_CuandoUsuarioNoExiste_DebeLanzarUsernameNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        when(usuarioRepo.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );

        assertEquals("Usuario no encontrado: " + username, exception.getMessage());
        verify(usuarioRepo, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_CuandoUsuarioEstaInactivo_DebeLanzarUsernameNotFoundException() {
        // Arrange
        String username = "inactiveuser";
        when(usuarioRepo.findByUsername(username)).thenReturn(Optional.of(usuarioInactivo));

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );

        assertEquals("Usuario inactivo", exception.getMessage());
        verify(usuarioRepo, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_CuandoUsernameEsNull_DebeLanzarExcepcion() {
        // Arrange
        when(usuarioRepo.findByUsername(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null)
        );

        verify(usuarioRepo, times(1)).findByUsername(null);
    }

    @Test
    void loadUserByUsername_CuandoUsernameEsVacio_DebeLanzarExcepcion() {
        // Arrange
        String emptyUsername = "";
        when(usuarioRepo.findByUsername(emptyUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(emptyUsername)
        );

        verify(usuarioRepo, times(1)).findByUsername(emptyUsername);
    }

    @Test
    void loadUserByUsername_DebeAsignarRolUSER() {
        // Arrange
        String username = "testuser";
        when(usuarioRepo.findByUsername(username)).thenReturn(Optional.of(usuarioActivo));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(usuarioRepo, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_DebeMantenerHashOriginal() {
        // Arrange
        String username = "testuser";
        String expectedHash = "$2a$10$hashedPassword";
        when(usuarioRepo.findByUsername(username)).thenReturn(Optional.of(usuarioActivo));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(expectedHash, userDetails.getPassword());
        verify(usuarioRepo, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_CuandoRepositorioLanzaExcepcion_DebePropagarExcepcion() {
        // Arrange
        String username = "testuser";
        when(usuarioRepo.findByUsername(username))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );

        verify(usuarioRepo, times(1)).findByUsername(username);
    }
}