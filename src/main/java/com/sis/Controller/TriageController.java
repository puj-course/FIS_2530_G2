package com.sis.Controller;

import com.sis.Model.Triage;
import com.sis.Model.TicketAdmision;
import com.sis.Model.Enum.NivelTriage;
import com.sis.Model.Enum.EstadoTicket;
import com.sis.Model.Enum.TipoEvento;
import com.sis.Repository.TriageRepo;
import com.sis.Repository.TicketRepo;
import com.sis.Service.EventBus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/triage")
public class TriageController {

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private TriageRepo triageRepo;

    @Autowired
    private EventBus eventBus;

    @GetMapping("/pendientes")
    public String listarTicketsPendientes(Model model, HttpSession session) {
        try {
            // Obtener tickets en estado EN_TRIAGE o PENDIENTE
            List<TicketAdmision> ticketsPendientes = ticketRepo.findByEstado(EstadoTicket.PENDIENTE);
            List<TicketAdmision> ticketsEnTriage = ticketRepo.findByEstado(EstadoTicket.EN_TRIAGE);

            model.addAttribute("ticketsPendientes", ticketsPendientes);
            model.addAttribute("ticketsEnTriage", ticketsEnTriage);

            return "triage/lista-pendientes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar tickets: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/formulario/{ticketId}")
    public String mostrarFormularioTriage(@PathVariable UUID ticketId, Model model) {
        try {
            TicketAdmision ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new Exception("Ticket no encontrado"));

            // Verificar si ya existe un triage para este ticket
            Triage triageExistente = triageRepo.findByTicket(ticketId).orElse(null);

            if (triageExistente != null) {
                model.addAttribute("triage", triageExistente);
                model.addAttribute("esEdicion", true);
            } else {
                model.addAttribute("triage", new Triage());
                model.addAttribute("esEdicion", false);
            }

            model.addAttribute("ticket", ticket);
            model.addAttribute("nivelesTriage", NivelTriage.values());

            return "triage/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "redirect:/triage/pendientes";
        }
    }

    @PostMapping("/registrar")
    public String registrarTriage(
            @RequestParam UUID ticketId,
            @RequestParam String sintomas,
            @RequestParam(required = false) BigDecimal temperaturaC,
            @RequestParam(required = false) Integer frecuenciaCardiaca,
            @RequestParam(required = false) Integer presionSistolica,
            @RequestParam(required = false) Integer presionDiastolica,
            @RequestParam(required = false) Integer saturacionO2,
            HttpSession session,
            Model model) {

        try {
            TicketAdmision ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new Exception("Ticket no encontrado"));

            // Crear nuevo triage
            Triage triage = new Triage();
            triage.setTicket(ticket);
            triage.setSintomas(sintomas);
            triage.setTemperaturaC(temperaturaC);
            triage.setFrecuenciaCardiaca(frecuenciaCardiaca);
            triage.setPresionSistolica(presionSistolica);
            triage.setPresionDiastolica(presionDiastolica);
            triage.setSaturacionO2(saturacionO2);

            // Calcular prioridad automáticamente basado en signos vitales
            triage.calcularPrioridad();

            // Guardar triage
            Triage triageGuardado = triageRepo.save(triage);

            // Actualizar estado del ticket
            ticket.setEstado(EstadoTicket.EN_TRIAGE);
            ticketRepo.save(ticket);

            // Publicar evento de triage completado
            eventBus.publicar(TipoEvento.TRIAGE_COMPLETADO, triageGuardado);
            eventBus.publicar(TipoEvento.NUEVO_TRIAGE,
                    "Triage completado para paciente: " + ticket.getPaciente().getNombreCompleto());

            model.addAttribute("mensaje", "Triage registrado exitosamente con prioridad: " + triageGuardado.getPrioridad());
            return "redirect:/triage/pendientes";

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar triage: " + e.getMessage());
            return mostrarFormularioTriage(ticketId, model);
        }
    }

    @PostMapping("/asignar-prioridad")
    @ResponseBody
    public String asignarPrioridad(
            @RequestParam UUID triageId,
            @RequestParam NivelTriage prioridad) {

        try {
            Triage triage = triageRepo.findById(triageId)
                    .orElseThrow(() -> new Exception("Triage no encontrado"));

            // Asignar prioridad manualmente (override automático)
            triage.setPrioridad(prioridad);
            triageRepo.save(triage);

            // Publicar evento de cambio de prioridad
            eventBus.publicar(TipoEvento.PRIORIDAD_ACTUALIZADA, triage);

            return "{\"success\": true, \"message\": \"Prioridad actualizada a " + prioridad + "\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/calcular-prioridad")
    @ResponseBody
    public String calcularPrioridadAutomatica(
            @RequestParam(required = false) BigDecimal temperaturaC,
            @RequestParam(required = false) Integer saturacionO2,
            @RequestParam(required = false) Integer presionSistolica) {

        try {
            Triage triageTemporal = new Triage();
            triageTemporal.setTemperaturaC(temperaturaC);
            triageTemporal.setSaturacionO2(saturacionO2);
            triageTemporal.setPresionSistolica(presionSistolica);

            triageTemporal.calcularPrioridad();

            NivelTriage prioridadCalculada = triageTemporal.getPrioridad();

            return "{\"success\": true, \"prioridad\": \"" + prioridadCalculada + "\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error al calcular: " + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/detalle/{triageId}")
    public String verDetalleTriage(@PathVariable UUID triageId, Model model) {
        try {
            Triage triage = triageRepo.findById(triageId)
                    .orElseThrow(() -> new Exception("Triage no encontrado"));

            model.addAttribute("triage", triage);
            return "triage/detalle";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalle: " + e.getMessage());
            return "redirect:/triage/pendientes";
        }
    }

    @GetMapping("/por-prioridad")
    public String listarPorPrioridad(@RequestParam NivelTriage prioridad, Model model) {
        try {
            List<Triage> triages = triageRepo.findByPrioridad(prioridad);
            model.addAttribute("triages", triages);
            model.addAttribute("prioridad", prioridad);
            return "triage/lista-por-prioridad";
        } catch (Exception e) {
            model.addAttribute("error", "Error al filtrar: " + e.getMessage());
            return "redirect:/triage/pendientes";
        }
    }
}