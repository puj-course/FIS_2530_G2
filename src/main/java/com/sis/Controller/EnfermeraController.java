package com.sis.Controller;

import com.sis.DTO.TicketAdmisionFormDTO;
import com.sis.DTO.TriageFormDTO;
import com.sis.Model.*;
import com.sis.Model.Enum.EstadoTicket;
import com.sis.Model.Enum.NivelTriage;
import com.sis.Repository.UsuarioRepo;
import com.sis.Service.AdmisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/enfermera")
public class EnfermeraController {

    @Autowired
    private AdmisionService admisionService;

    @Autowired
    private UsuarioRepo usuarioRepo;

    /**
     * Dashboard principal de enfermera
     * RUTA: /enfermera/dashboard
     */
    @GetMapping("/dashboard") // <--- ¡CAMBIO REALIZADO AQUÍ! Ahora mapea a /enfermera/dashboard
    public String dashboard(Authentication auth, Model model) {
        String username = auth.getName();
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!(usuario instanceof Enfermera)) {
            throw new RuntimeException("El usuario no es una enfermera");
        }

        Enfermera enfermera = (Enfermera) usuario;

        // Estadísticas
        long ticketsPendientes = admisionService.contarPorEstado(EstadoTicket.PENDIENTE);
        long ticketsEnTriage = admisionService.contarPorEstado(EstadoTicket.EN_TRIAGE);
        long misTicketsTotal = admisionService.contarPorEnfermera(enfermera.getId());

        model.addAttribute("enfermera", enfermera);
        model.addAttribute("ticketsPendientes", ticketsPendientes);
        model.addAttribute("ticketsEnTriage", ticketsEnTriage);
        model.addAttribute("misTicketsTotal", misTicketsTotal);

        return "enfermera/dashboard";
    }


    @GetMapping("/admisiones")
    public String listarAdmisiones(@RequestParam(required = false) String estado, Model model) {
        List<TicketAdmision> tickets;

        if (estado != null && !estado.isEmpty()) {
            EstadoTicket estadoEnum = EstadoTicket.valueOf(estado);
            tickets = admisionService.listarPorEstado(estadoEnum);
            model.addAttribute("estadoFiltro", estado);
        } else {
            tickets = admisionService.listarTodos();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("estados", EstadoTicket.values());

        return "enfermera/admisiones-lista";
    }

    /**
     * Formulario para nueva admisión
     */
    @GetMapping("/admisiones/nueva")
    public String nuevaAdmision(Model model) {
        model.addAttribute("form", new TicketAdmisionFormDTO());
        model.addAttribute("pacientes", admisionService.listarPacientesActivos());
        return "enfermera/admisiones-form";
    }

    /**
     * Crear nueva admisión (POST)
     */
    @PostMapping("/admisiones")
    public String crearAdmision(@ModelAttribute("form") TicketAdmisionFormDTO form,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            TicketAdmision ticket = admisionService.crearAdmision(form, auth.getName());
            redirectAttributes.addFlashAttribute("mensaje", "Ticket creado exitosamente");

            // Redirigir al formulario de triage
            return "redirect:/enfermera/triage/" + ticket.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear ticket: " + e.getMessage());
            return "redirect:/enfermera/admisiones/nueva";
        }
    }

    /**
     * Ver mis tickets (los creados por la enfermera logueada)
     */
    @GetMapping("/mis-tickets")
    public String misTickets(Authentication auth, Model model) {
        String username = auth.getName();
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!(usuario instanceof Enfermera)) {
            throw new RuntimeException("El usuario no es una enfermera");
        }

        Enfermera enfermera = (Enfermera) usuario;

        List<TicketAdmision> tickets = admisionService.listarPorEnfermera(enfermera.getId());
        model.addAttribute("tickets", tickets);

        return "enfermera/mis-tickets";
    }

    /**
     * Ver detalle de un ticket
     */
    @GetMapping("/ticket/{id}")
    public String verTicket(@PathVariable UUID id, Model model) {
        TicketAdmision ticket = admisionService.obtenerTicket(id);
        Triage triage = admisionService.obtenerTriagePorTicket(id);

        model.addAttribute("ticket", ticket);
        model.addAttribute("paciente", ticket.getPaciente());
        model.addAttribute("triage", triage);

        return "enfermera/ticket-detalle";
    }

    /**
     * Formulario de triage
     */
    @GetMapping("/triage/{ticketId}")
    public String formularioTriage(@PathVariable UUID ticketId, Model model, RedirectAttributes redirectAttributes) {
        try {
            TicketAdmision ticket = admisionService.obtenerTicket(ticketId);

            // Verificar si ya tiene triage
            Triage triageExistente = admisionService.obtenerTriagePorTicket(ticketId);
            if (triageExistente != null) {
                redirectAttributes.addFlashAttribute("error", "Este ticket ya tiene triage registrado");
                return "redirect:/enfermera/ticket/" + ticketId;
            }

            TriageFormDTO form = new TriageFormDTO();
            form.setTicketId(ticketId);

            model.addAttribute("ticket", ticket);
            model.addAttribute("paciente", ticket.getPaciente());
            model.addAttribute("triageForm", form);
            model.addAttribute("nivelesTriage", NivelTriage.values());

            return "enfermera/triage-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/enfermera/admisiones";
        }
    }

    /**
     * Registrar triage (POST)
     */
    @PostMapping("/triage")
    public String registrarTriage(@ModelAttribute("triageForm") TriageFormDTO form,
                                  RedirectAttributes redirectAttributes) {
        try {
            admisionService.registrarTriage(form);
            redirectAttributes.addFlashAttribute("mensaje", "Triage registrado exitosamente");
            return "redirect:/enfermera/ticket/" + form.getTicketId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar triage: " + e.getMessage());
            return "redirect:/enfermera/triage/" + form.getTicketId();
        }
    }
}