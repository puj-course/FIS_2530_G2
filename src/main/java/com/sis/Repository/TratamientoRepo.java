package com.sis.Repository;

import com.sis.Model.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TratamientoRepo extends JpaRepository<Tratamiento, UUID> {

    Tratamiento save(Tratamiento tratamiento);
    Optional<Tratamiento> findById(UUID id);

    @Query("SELECT t FROM Tratamiento t WHERE t.diagnostico.id = :diagnosticoId ORDER BY t.creadoEn DESC")
    List<Tratamiento> findByDiagnostico(@Param("diagnosticoId") UUID diagnosticoId);

    // NUEVOS MÉTODOS
    @Query("SELECT t FROM Tratamiento t WHERE t.diagnostico.consulta.paciente.id = :pacienteId ORDER BY t.creadoEn DESC")
    List<Tratamiento> findByPaciente(@Param("pacienteId") UUID pacienteId);
}