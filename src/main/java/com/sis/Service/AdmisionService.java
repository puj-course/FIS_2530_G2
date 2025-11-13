package com.sis.Service;

import com.sis.DTO.TicketAdmisionFormDTO;
import com.sis.DTO.TriageFormDTO;
import com.sis.Model.*;
        import com.sis.Model.Enum.EstadoTicket;
import com.sis.Repository.*;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AdmisionService {

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private TriageRepo triageRepo;

    @Autowired
    private PacienteRepo pacienteRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    /**
     * Crear un nuevo ticket de admisión
     */
    @Transactional
    public TicketAdmision crearAdmision(TicketAdmisionFormDTO dto, String usernameEnfermera) {
        // Buscar enfermera
        Usuario usuario = usuarioRepo.findByUsername(usernameEnfermera)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!(usuario instanceof Enfermera)) {
            throw new RuntimeException("El usuario no es una enfermera");
        }

        Enfermera enfermera = (Enfermera) usuario;

        // Buscar paciente
        Paciente paciente = pacienteRepo.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Crear ticket
        TicketAdmision ticket = new TicketAdmision();
        ticket.setPaciente(paciente);
        ticket.setEnfermera(enfermera);
        ticket.setEstado(EstadoTicket.PENDIENTE);

        return ticketRepo.save(ticket);
    }

    /**
     * Registrar triage para un ticket
     */
    @Transactional
    public Triage registrarTriage(TriageFormDTO dto) {
        // Buscar ticket
        TicketAdmision ticket = ticketRepo.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        // Verificar que no tenga triage ya
        if (triageRepo.findByTicket(ticket.getId()).isPresent()) {
            throw new RuntimeException("Este ticket ya tiene triage registrado");
        }

        // Crear triage
        Triage triage = new Triage();
        triage.setTicket(ticket);
        triage.setSintomas(dto.getSintomas());
        triage.setTemperaturaC(dto.getTemperaturaC());
        triage.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        triage.setPresionSistolica(dto.getPresionSistolica());
        triage.setPresionDiastolica(dto.getPresionDiastolica());
        triage.setSaturacionO2(dto.getSaturacionO2());

        // Calcular prioridad automáticamente si no viene
        if (dto.getPrioridad() != null) {
            triage.setPrioridad(dto.getPrioridad());
        } else {
            triage.calcularPrioridad();
        }

        // Actualizar estado del ticket
        ticket.setEstado(EstadoTicket.EN_TRIAGE);
        ticketRepo.save(ticket);

        return triageRepo.save(triage);
    }

    /**
     * Listar todos los tickets
     */
    public List<TicketAdmision> listarTodos() {
        return ticketRepo.findAll();
    }

    /**
     * Listar tickets por estado
     */
    public List<TicketAdmision> listarPorEstado(EstadoTicket estado) {
        return ticketRepo.findByEstado(estado);
    }

    /**
     * Listar tickets de una enfermera específica
     */
    public List<TicketAdmision> listarPorEnfermera(UUID enfermeraId) {
        return ticketRepo.findByEnfermera(enfermeraId);
    }

    /**
     * Obtener un ticket por ID
     */
    public TicketAdmision obtenerTicket(UUID ticketId) {
        return ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
    }

    /**
     * Obtener triage de un ticket
     */
    public Triage obtenerTriagePorTicket(UUID ticketId) {
        return triageRepo.findByTicket(ticketId).orElse(null);
    }

    /**
     * Contar tickets por estado
     */
    public long contarPorEstado(EstadoTicket estado) {
        return ticketRepo.countByEstado(estado);
    }

    /**
     * Contar tickets de una enfermera
     */
    public long contarPorEnfermera(UUID enfermeraId) {
        return ticketRepo.findByEnfermera(enfermeraId).size();
    }

    /**
     * Listar todos los pacientes activos
     */
    public List<Paciente> listarPacientesActivos() {
        return pacienteRepo.findAllActivos();
    }
}