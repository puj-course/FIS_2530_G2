package com.sis.Repository;

import com.sis.Model.TicketAdmision;
import com.sis.Model.Enum.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepo extends JpaRepository<TicketAdmision, UUID> {

    TicketAdmision save(TicketAdmision ticketAdmision);
    List<TicketAdmision> findByEstado(EstadoTicket estado);
    List<TicketAdmision> findByPaciente(UUID pacienteId);
}