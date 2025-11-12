package com.sis.Controller;

import com.sis.Model.Consulta;
import com.sis.Model.Paciente;
import com.sis.Model.Doctor;
import com.sis.Model.TicketAdmision;
import com.sis.Model.Enum.EstadoTicket;
import com.sis.Patterns.Strategy.IExportadorHistoria;
import com.sis.Patterns.Strategy.ExportadorHTML;
import com.sis.Patterns.Strategy.ExportadorPDF;
import com.sis.Patterns.Prototype.PacientePrototype;
import com.sis.Repository.ConsultaRepo;
import com.sis.Repository.PacienteRepo;
import com.sis.Repository.TicketRepo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaRepo consultaRepo;

    @Autowired
    private PacienteRepo pacienteRepo;

    @Autowired
    private TicketRepo ticketRepo;

    private IExportadorHistoria exportador;

    @GetMapping("/pendientes")
    public String listarConsultasPendientes(Model model, HttpSession session) {
        try {
            List<TicketAdmision> ticketsPendientes = ticketRepo.findByEstado(EstadoTicket.EN_TRIAGE);
            model.addAttribute("tickets", ticketsPendientes);
            return "consulta/lista-pendientes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar consultas: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/iniciar/{ticketId}")
    public String iniciarConsulta(
            @PathVariable UUID ticketId,
            Model model,
            HttpSession session) {

        try {
            TicketAdmision ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new Exception("Ticket no encontrado"));

            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            // Crear nueva consulta
            Consulta consulta = new Consulta();
            consulta.setTicket(ticket);
            consulta.setPaciente(ticket.getPaciente());
            consulta.setDoctor(doctor);
            consulta.setEnfermera(ticket.getEnfermera());
            consulta.setFechaHora(LocalDateTime.now());

            Consulta consultaGuardada = consultaRepo.save(consulta);

            // Actualizar estado del ticket
            ticket.setEstado(EstadoTicket.EN_CONSULTA);
            ticketRepo.save(ticket);

            model.addAttribute("consulta", consultaGuardada);
            return "redirect:/consulta/formulario/" + consultaGuardada.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar consulta: " + e.getMessage());
            return "redirect:/consulta/pendientes";
        }
    }

    @GetMapping("/formulario/{consultaId}")
    public String mostrarFormularioConsulta(@PathVariable UUID consultaId, Model model) {
        try {
            Consulta consulta = consultaRepo.findById(consultaId)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            model.addAttribute("consulta", consulta);
            model.addAttribute("paciente", consulta.getPaciente());
            model.addAttribute("triage", consulta.getTicket().getTriage());

            return "consulta/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar consulta: " + e.getMessage());
            return "redirect:/consulta/pendientes";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarConsulta(
            @RequestParam UUID consultaId,
            @RequestParam String motivo,
            Model model) {

        try {
            Consulta consulta = consultaRepo.findById(consultaId)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            consulta.setMotivo(motivo);
            consultaRepo.save(consulta);

            model.addAttribute("mensaje", "Consulta actualizada exitosamente");
            return "redirect:/diagnostico/registrar/" + consultaId;

        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar consulta: " + e.getMessage());
            return mostrarFormularioConsulta(consultaId, model);
        }
    }

    @GetMapping("/historial/{pacienteId}")
    public String consultarHistorial(@PathVariable UUID pacienteId, Model model) {
        try {
            Paciente paciente = pacienteRepo.findById(pacienteId)
                    .orElseThrow(() -> new Exception("Paciente no encontrado"));

            List<Consulta> consultas = consultaRepo.findByPaciente(pacienteId);

            model.addAttribute("paciente", paciente);
            model.addAttribute("consultas", consultas);

            return "consulta/historial";
        } catch (Exception e) {
            model.addAttribute("error", "Error al consultar historial: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/exportar/{pacienteId}")
    public void exportarHistorial(
            @PathVariable UUID pacienteId,
            @RequestParam(defaultValue = "html") String formato,
            HttpServletResponse response) throws IOException {

        try {
            Paciente paciente = pacienteRepo.findById(pacienteId)
                    .orElseThrow(() -> new Exception("Paciente no encontrado"));

            List<Consulta> consultas = consultaRepo.findByPaciente(pacienteId);

            // Strategy Pattern: Seleccionar exportador según formato
            if (formato.equalsIgnoreCase("pdf")) {
                exportador = new ExportadorPDF();
            } else {
                exportador = new ExportadorHTML();
            }

            String contenido = exportador.exportar(paciente, consultas);

            // Configurar respuesta HTTP
            response.setContentType(exportador.getMimeType());
            response.setHeader("Content-Disposition",
                    "attachment; filename=historial_" + paciente.getNumeroDocumento() +
                            "." + exportador.getExtension());

            response.getWriter().write(contenido);
            response.getWriter().flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al exportar: " + e.getMessage());
        }
    }

    @GetMapping("/mis-consultas")
    public String listarMisConsultas(Model model, HttpSession session) {
        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login";
            }

            List<Consulta> consultas = consultaRepo.findByDoctor(doctor.getId());
            model.addAttribute("consultas", consultas);

            return "consulta/mis-consultas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar consultas: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/clonar-paciente")
    @ResponseBody
    public String clonarPacienteProvisional(@RequestParam UUID pacienteId) {
        try {
            Paciente pacienteOriginal = pacienteRepo.findById(pacienteId)
                    .orElseThrow(() -> new Exception("Paciente no encontrado"));

            // Prototype Pattern: Clonar paciente para registro rápido
            PacientePrototype prototype = new PacientePrototype(pacienteOriginal);
            Paciente pacienteClonado = prototype.clone();

            Paciente guardado = pacienteRepo.save(pacienteClonado);

            return "{\"success\": true, \"message\": \"Paciente clonado\", \"pacienteId\": \"" + guardado.getId() + "\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/finalizar/{consultaId}")
    public String finalizarConsulta(@PathVariable UUID consultaId, Model model) {
        try {
            Consulta consulta = consultaRepo.findById(consultaId)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            // Actualizar estado del ticket a COMPLETADO
            TicketAdmision ticket = consulta.getTicket();
            ticket.setEstado(EstadoTicket.COMPLETADO);
            ticketRepo.save(ticket);

            model.addAttribute("mensaje", "Consulta finalizada exitosamente");
            return "redirect:/consulta/pendientes";

        } catch (Exception e) {
            model.addAttribute("error", "Error al finalizar consulta: " + e.getMessage());
            return "redirect:/consulta/pendientes";
        }
    }
}