package com.sis.Service;

import com.sis.Model.Usuario;
import com.sis.Repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        // Aquí podrías agregar roles si tienes una tabla de roles
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getHash()) // Spring Security espera el hash
                .disabled(!usuario.isActivo())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .roles("USER") // Por ahora rol genérico, puedes mejorarlo
                .build();
    }
}