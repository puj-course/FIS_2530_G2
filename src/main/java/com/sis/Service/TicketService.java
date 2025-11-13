package com.sis.Service;

import com.sis.Model.TicketAdmision;
import com.sis.Model.Enum.EstadoTicket;
import com.sis.Repository.TicketRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepo ticketRepository;

    public TicketService(TicketRepo ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketAdmision crearTicket(TicketAdmision ticket) {
        return ticketRepository.save(ticket);
    }

    public Optional<TicketAdmision> obtenerTicketPorId(UUID ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public List<TicketAdmision> listarTicketsPorEstado(EstadoTicket estado) {
        return ticketRepository.findByEstado(estado);
    }

    public TicketAdmision actualizarTicket(TicketAdmision ticket) {
        return ticketRepository.save(ticket);
    }

    public List<TicketAdmision> listarTodosLosTickets() {
        return ticketRepository.findAll();
    }

    public void eliminarTicket(UUID ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}