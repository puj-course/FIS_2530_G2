package com.sis.Repository;

import com.sis.Model.Usuario;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario,UUID> {
    Optional<Usuario> findById(UUID id);
    Usuario save(Usuario usuario);
    Optional<Usuario> findByDocumento(String tipoDocumento, String numeroDocumento);
}
