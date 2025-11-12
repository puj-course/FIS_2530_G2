package com.sis.Controller;

import com.sis.Model.TicketAdmision;
import com.sis.Model.Paciente;
import com.sis.Model.Enfermera;
import com.sis.Model.Enum.EstadoTicket;
import com.sis.Model.Enum.TipoEvento;
import com.sis.Repository.TicketRepo;
import com.sis.Repository.PacienteRepo;
import com.sis.Service.EventBus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.UUID;

/**
 * Controlador para gestionar el proceso de admisión de pacientes
 */
@Controller
@RequestMapping("/admision")
public class AdmisionController {

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private PacienteRepo pacienteRepo;

    @Autowired
    private EventBus eventBus;

    /**
     * Muestra el formulario para crear un nuevo ticket de admisión
     */
    @GetMapping("/crear-ticket")
    public String mostrarFormularioTicket(
            @RequestParam(required = false) UUID pacienteId,
            Model model) {

        try {
            if (pacienteId != null) {
                Paciente paciente = pacienteRepo.findById(pacienteId)
                        .orElseThrow(() -> new Exception("Paciente no encontrado"));
                model.addAttribute("paciente", paciente);
            }

            // Lista de pacientes para seleccionar
            List<Paciente> pacientes = pacienteRepo.findAllActivos();
            model.addAttribute("pacientes", pacientes);

            return "admision/crear-ticket";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "redirect:/admision/dashboard";
        }
    }

    /**
     * Crea un nuevo ticket de admisión
     */
    @PostMapping("/crear-ticket")
    public String crearTicket(
            @RequestParam UUID pacienteId,
            HttpSession session,
            Model model) {

        try {
            Paciente paciente = pacienteRepo.findById(pacienteId)
                    .orElseThrow(() -> new Exception("Paciente no encontrado"));

            Enfermera enfermera = (Enfermera) session.getAttribute("usuario");
            if (enfermera == null) {
                return "redirect:/login?error=sesion";
            }

            // Crear ticket de admisión
            TicketAdmision ticket = new TicketAdmision();
            ticket.setPaciente(paciente);
            ticket.setEnfermera(enfermera);
            ticket.setEstado(EstadoTicket.PENDIENTE);

            TicketAdmision ticketGuardado = ticketRepo.save(ticket);

            // Publicar evento usando Observer Pattern
            eventBus.publicar(TipoEvento.TICKET_CREADO, ticketGuardado);
            eventBus.publicar(TipoEvento.PACIENTE_LLEGADA,
                    "Paciente: " + paciente.getNombreCompleto() + " - Ticket: " + ticketGuardado.getId());

            model.addAttribute("mensaje", "Ticket creado exitosamente");
            model.addAttribute("ticketId", ticketGuardado.getId());

            return "redirect:/triage/formulario/" + ticketGuardado.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear ticket: " + e.getMessage());
            return mostrarFormularioTicket(pacienteId, model);
        }
    }

    /**
     * Dashboard de admisión con estadísticas
     */
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        try {
            Enfermera enfermera = (Enfermera) session.getAttribute("usuario");

            // Estadísticas generales
            long ticketsPendientes = ticketRepo.countByEstado(EstadoTicket.PENDIENTE);
            long ticketsEnTriage = ticketRepo.countByEstado(EstadoTicket.EN_TRIAGE);
            long ticketsEnConsulta = ticketRepo.countByEstado(EstadoTicket.EN_CONSULTA);
            long ticketsCompletados = ticketRepo.countByEstado(EstadoTicket.COMPLETADO);

            model.addAttribute("ticketsPendientes", ticketsPendientes);
            model.addAttribute("ticketsEnTriage", ticketsEnTriage);
            model.addAttribute("ticketsEnConsulta", ticketsEnConsulta);
            model.addAttribute("ticketsCompletados", ticketsCompletados);

            // Tickets de la enfermera actual
            if (enfermera != null) {
                List<TicketAdmision> misTickets = ticketRepo.findByEnfermera(enfermera.getId());
                model.addAttribute("misTickets", misTickets);
            }

            return "admision/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar dashboard: " + e.getMessage());
            return "error/500";
        }
    }

    /**
     * Lista todos los tickets de admisión
     */
    @GetMapping("/tickets")
    public String listarTickets(
            @RequestParam(required = false) EstadoTicket estado,
            Model model) {

        try {
            List<TicketAdmision> tickets;

            if (estado != null) {
                tickets = ticketRepo.findByEstado(estado);
            } else {
                tickets = ticketRepo.findAll();
            }

            model.addAttribute("tickets", tickets);
            model.addAttribute("estados", EstadoTicket.values());
            model.addAttribute("estadoFiltro", estado);

            return "admision/lista-tickets";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar tickets: " + e.getMessage());
            return "redirect:/admision/dashboard";
        }
    }

    /**
     * Ver detalle de un ticket
     */
    @GetMapping("/ticket/{ticketId}")
    public String verDetalleTicket(@PathVariable UUID ticketId, Model model) {
        try {
            TicketAdmision ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new Exception("Ticket no encontrado"));

            model.addAttribute("ticket", ticket);
            model.addAttribute("paciente", ticket.getPaciente());
            model.addAttribute("enfermera", ticket.getEnfermera());

            return "admision/detalle-ticket";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalle: " + e.getMessage());
            return "redirect:/admision/tickets";
        }
    }

    /**
     * Actualizar estado de un ticket
     */
    @PostMapping("/ticket/actualizar-estado")
    @ResponseBody
    public String actualizarEstadoTicket(
            @RequestParam UUID ticketId,
            @RequestParam EstadoTicket nuevoEstado) {

        try {
            TicketAdmision ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new Exception("Ticket no encontrado"));

            ticket.setEstado(nuevoEstado);
            ticketRepo.save(ticket);

            // Publicar evento
            eventBus.publicar(TipoEvento.ESTADO_TICKET_ACTUALIZADO, ticket);

            return "{\"success\": true, \"message\": \"Estado actualizado a " + nuevoEstado + "\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}";
        }
    }

    /**
     * Cancelar un ticket
     */
    @PostMapping("/ticket/cancelar/{ticketId}")
    public String cancelarTicket(@PathVariable UUID ticketId, Model model) {
        try {
            TicketAdmision ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new Exception("Ticket no encontrado"));

            ticket.setEstado(EstadoTicket.CANCELAD);
            ticketRepo.save(ticket);

            // Publicar evento
            eventBus.publicar(TipoEvento.TICKET_CANCELADO, ticket);

            model.addAttribute("mensaje", "Ticket cancelado exitosamente");
            return "redirect:/admision/tickets";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cancelar ticket: " + e.getMessage());
            return "redirect:/admision/ticket/" + ticketId;
        }
    }

    /**
     * Buscar pacientes para admisión
     */
    @GetMapping("/buscar-paciente")
    @ResponseBody
    public List<Paciente> buscarPaciente(@RequestParam String query) {
        try {
            // Buscar por nombre o número de documento
            List<Paciente> porNombre = pacienteRepo.buscarPorNombre(query);

            if (!porNombre.isEmpty()) {
                return porNombre;
            }

            // Si no encuentra por nombre, buscar por documento
            return pacienteRepo.findByNumeroDocumento(query)
                    .map(List::of)
                    .orElse(List.of());
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Mis tickets (para enfermeras)
     */
    @GetMapping("/mis-tickets")
    public String listarMisTickets(Model model, HttpSession session) {
        try {
            Enfermera enfermera = (Enfermera) session.getAttribute("usuario");
            if (enfermera == null) {
                return "redirect:/login";
            }

            List<TicketAdmision> misTickets = ticketRepo.findByEnfermera(enfermera.getId());
            model.addAttribute("tickets", misTickets);

            return "admision/mis-tickets";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar tickets: " + e.getMessage());
            return "redirect:/admision/dashboard";
        }
    }

    /**
     * Tickets por paciente
     */
    @GetMapping("/paciente/{pacienteId}/tickets")
    public String listarTicketsPorPaciente(@PathVariable UUID pacienteId, Model model) {
        try {
            Paciente paciente = pacienteRepo.findById(pacienteId)
                    .orElseThrow(() -> new Exception("Paciente no encontrado"));

            List<TicketAdmision> tickets = ticketRepo.findByPaciente(pacienteId);

            model.addAttribute("paciente", paciente);
            model.addAttribute("tickets", tickets);

            return "admision/tickets-paciente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar tickets: " + e.getMessage());
            return "redirect:/admision/tickets";
        }
    }
}