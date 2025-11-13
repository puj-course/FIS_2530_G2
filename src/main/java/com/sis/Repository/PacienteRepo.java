package com.sis.Repository;

import com.sis.Model.Paciente;
import com.sis.Model.Enum.Aseguradora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepo extends JpaRepository<Paciente, UUID> {

    Optional<Paciente> findById(UUID id);
    Paciente save(Paciente paciente);
    List<Paciente> findAll();

    // NUEVOS MÉTODOS
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);
    Optional<Paciente> findByUsername(String username);
    Optional<Paciente> findByTelefono(String telefono);

    List<Paciente> findByEsProvisional(boolean esProvisional);
    List<Paciente> findBySeguro(Aseguradora seguro);

    @Query("SELECT p FROM Paciente p WHERE p.activo = TRUE")
    List<Paciente> findAllActivos();

    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombres) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Paciente> buscarPorNombre(@Param("nombre") String nombre);
}