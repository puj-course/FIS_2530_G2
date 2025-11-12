package com.sis.Service;

import com.sis.DTO.UsuarioRegistroDTO;
import com.sis.Model.Usuario;
import com.sis.Repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsuarioRegistroService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Valida los datos del formulario de registro
     * @param dto Datos del usuario a registrar
     * @return Map con errores (vacío si no hay errores)
     */
    public Map<String, String> validarRegistro(UsuarioRegistroDTO dto) {
        Map<String, String> errores = new HashMap<>();

        // Validar que las contraseñas coincidan
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            errores.put("password", "Las contraseñas no coinciden");
        }

        // Validar username único
        if (usuarioRepo.existsByUsername(dto.getUsername())) {
            errores.put("username", "El nombre de usuario ya existe");
        }

        // Validar documento único
        if (usuarioRepo.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            errores.put("numeroDocumento", "El número de documento ya está registrado");
        }

        // Validar email único (si se proporciona)
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            if (usuarioRepo.existsByEmail(dto.getEmail())) {
                errores.put("email", "El email ya está registrado");
            }
        }

        return errores;
    }

    /**
     * Registra un nuevo usuario en el sistema
     * @param dto Datos del usuario a registrar
     * @return Usuario registrado
     */
    @Transactional
    public Usuario registrarUsuario(UsuarioRegistroDTO dto) {
        // Crear nueva instancia de Usuario
        Usuario usuario = new Usuario();

        // Mapear datos básicos
        usuario.setUsername(dto.getUsername());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setTipoDocumento(dto.getTipoDocumento());
        usuario.setNumeroDocumento(dto.getNumeroDocumento());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTipoUsuario(dto.getTipoUsuario());

        // ⭐ IMPORTANTE: Hashear la contraseña con BCrypt antes de guardar
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        usuario.setHash(passwordHash);

        // Usuario activo por defecto
        usuario.setActivo(true);

        // Guardar en la base de datos
        return usuarioRepo.save(usuario);
    }

    /**
     * Verifica si un username ya existe
     */
    public boolean usernameExiste(String username) {
        return usuarioRepo.existsByUsername(username);
    }

    /**
     * Verifica si un documento ya existe
     */
    public boolean documentoExiste(String numeroDocumento) {
        return usuarioRepo.existsByNumeroDocumento(numeroDocumento);
    }
}