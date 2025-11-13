package com.sis.Repository;

import com.sis.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findById(UUID id);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);
    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByNumeroDocumento(String numeroDocumento);
    boolean existsByEmail(String email);
}