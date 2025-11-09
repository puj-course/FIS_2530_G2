package com.sis.Repository;

import com.sis.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, UUID> { // ✅ CORREGIDO: era Integer

    Optional<Usuario> findById(UUID id); // ✅ CORREGIDO
    Optional<Usuario> findByUsername(String username); // ✅ NUEVO: para login
    Optional<Usuario> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    boolean existsByUsername(String username); // ✅ ÚTIL: para registro
}