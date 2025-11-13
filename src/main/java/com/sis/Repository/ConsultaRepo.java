package com.sis.Repository;

import com.sis.Model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultaRepo extends JpaRepository<Consulta, UUID> {

    Consulta save(Consulta consulta);
    Optional<Consulta> findById(UUID id);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId ORDER BY c.fechaHora DESC")
    List<Consulta> findByPaciente(@Param("pacienteId") UUID pacienteId);

    @Query("SELECT c FROM Consulta c WHERE c.doctor.id = :doctorId ORDER BY c.fechaHora DESC")
    List<Consulta> findByDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT c FROM Consulta c WHERE c.fechaHora BETWEEN :inicio AND :fin ORDER BY c.fechaHora DESC")
    List<Consulta> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio,
                                          @Param("fin") LocalDateTime fin);

    @Query("SELECT c FROM Consulta c WHERE c.ticket.id = :ticketId")
    Optional<Consulta> findByTicket(@Param("ticketId") UUID ticketId);

    @Query("SELECT c FROM Consulta c WHERE c.enfermera.id = :enfermeraId ORDER BY c.fechaHora DESC")
    List<Consulta> findByEnfermera(@Param("enfermeraId") UUID enfermeraId);

    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.fechaHora >= :fecha")
    long countConsultasDesde(@Param("fecha") LocalDateTime fecha);
}