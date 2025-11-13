package com.sis.Repository;

import com.sis.Model.TicketAdmision;
import com.sis.Model.Enum.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepo extends JpaRepository<TicketAdmision, UUID> {

    TicketAdmision save(TicketAdmision ticketAdmision);
    Optional<TicketAdmision> findById(UUID id);

    List<TicketAdmision> findByEstado(EstadoTicket estado);

    @Query("SELECT t FROM TicketAdmision t WHERE t.paciente.id = :pacienteId ORDER BY t.creadoEn DESC")
    List<TicketAdmision> findByPaciente(@Param("pacienteId") UUID pacienteId);

    // NUEVOS MÉTODOS
    @Query("SELECT t FROM TicketAdmision t WHERE t.enfermera.id = :enfermeraId ORDER BY t.creadoEn DESC")
    List<TicketAdmision> findByEnfermera(@Param("enfermeraId") UUID enfermeraId);

    @Query("SELECT t FROM TicketAdmision t WHERE t.estado = :estado ORDER BY t.creadoEn ASC")
    List<TicketAdmision> findByEstadoOrderByCreadoEnAsc(@Param("estado") EstadoTicket estado);

    @Query("SELECT t FROM TicketAdmision t WHERE t.creadoEn BETWEEN :inicio AND :fin ORDER BY t.creadoEn DESC")
    List<TicketAdmision> findByCreadoEnBetween(@Param("inicio") LocalDateTime inicio,
                                               @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(t) FROM TicketAdmision t WHERE t.estado = :estado")
    long countByEstado(@Param("estado") EstadoTicket estado);
}