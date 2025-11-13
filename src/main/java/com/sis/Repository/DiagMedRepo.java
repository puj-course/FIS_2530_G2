package com.sis.Repository;

import com.sis.Model.DiagnosticoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiagMedRepo extends JpaRepository<DiagnosticoMedicamento, UUID> {

    DiagnosticoMedicamento save(DiagnosticoMedicamento diagnosticoMedicamento);
    Optional<DiagnosticoMedicamento> findById(UUID id);

    @Query("SELECT dm FROM DiagnosticoMedicamento dm WHERE dm.diagnostico.id = :diagnosticoId ORDER BY dm.creadoEn DESC")
    List<DiagnosticoMedicamento> findByDiagnostico(@Param("diagnosticoId") UUID diagnosticoId);

    // NUEVOS MÉTODOS
    @Query("SELECT dm FROM DiagnosticoMedicamento dm WHERE dm.medicamento.id = :medicamentoId ORDER BY dm.creadoEn DESC")
    List<DiagnosticoMedicamento> findByMedicamento(@Param("medicamentoId") UUID medicamentoId);

    @Query("SELECT dm FROM DiagnosticoMedicamento dm WHERE dm.diagnostico.consulta.paciente.id = :pacienteId ORDER BY dm.creadoEn DESC")
    List<DiagnosticoMedicamento> findByPaciente(@Param("pacienteId") UUID pacienteId);

    @Query("SELECT dm.medicamento.nombre, COUNT(dm) FROM DiagnosticoMedicamento dm GROUP BY dm.medicamento.nombre ORDER BY COUNT(dm) DESC")
    List<Object[]> findMedicamentosMasPrescritos();
}