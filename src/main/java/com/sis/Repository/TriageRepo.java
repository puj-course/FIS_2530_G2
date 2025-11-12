package com.sis.Repository;

import com.sis.Model.Triage;
import com.sis.Model.Enum.NivelTriage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TriageRepo extends JpaRepository<Triage, UUID> {

    Triage save(Triage triage);

    @Query("SELECT t FROM Triage t WHERE t.ticket.id = :ticketId")
    Optional<Triage> findByTicket(@Param("ticketId") UUID ticketId);

    // NUEVOS MÉTODOS
    List<Triage> findByPrioridad(NivelTriage prioridad);

    @Query("SELECT t FROM Triage t WHERE t.prioridad = :prioridad ORDER BY t.registradoEn DESC")
    List<Triage> findByPrioridadOrderByRegistradoEnDesc(@Param("prioridad") NivelTriage prioridad);

    @Query("SELECT t FROM Triage t WHERE t.registradoEn BETWEEN :inicio AND :fin ORDER BY t.registradoEn DESC")
    List<Triage> findByRegistradoEnBetween(@Param("inicio") LocalDateTime inicio,
                                           @Param("fin") LocalDateTime fin);

    @Query("SELECT t FROM Triage t ORDER BY t.registradoEn DESC")
    List<Triage> findAllOrderByRegistradoEnDesc();
}


