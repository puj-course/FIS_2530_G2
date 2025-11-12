package com.sis.Repository;

import com.sis.Model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiagnosticoRepo extends JpaRepository<Diagnostico, UUID> {

    Diagnostico save(Diagnostico diagnostico);
    Optional<Diagnostico> findById(UUID id);

    @Query("SELECT d FROM Diagnostico d WHERE d.consulta.id = :consultaId ORDER BY d.creadoEn DESC")
    List<Diagnostico> findByConsulta(@Param("consultaId") UUID consultaId);

    // NUEVOS MÉTODOS
    @Query("SELECT d FROM Diagnostico d WHERE d.consulta.paciente.id = :pacienteId ORDER BY d.creadoEn DESC")
    List<Diagnostico> findByPaciente(@Param("pacienteId") UUID pacienteId);

    @Query("SELECT d FROM Diagnostico d WHERE d.consulta.doctor.id = :doctorId ORDER BY d.creadoEn DESC")
    List<Diagnostico> findByDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT d FROM Diagnostico d WHERE d.creadoEn BETWEEN :inicio AND :fin ORDER BY d.creadoEn DESC")
    List<Diagnostico> findByCreadoEnBetween(@Param("inicio") LocalDateTime inicio,
                                            @Param("fin") LocalDateTime fin);
}
