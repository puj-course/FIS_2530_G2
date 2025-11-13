package com.sis.Controller;

import com.sis.Model.*;
import com.sis.Model.Enum.EstadoTicket;
import com.sis.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller específico para operaciones del rol Doctor
 */
@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private DiagnosticoService diagnosticoService;

    @Autowired
    private TratamientoService tratamientoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String dashboard(Model model, HttpSession session) {
        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            List<Consulta> consultasRecientes = consultaService.listarConsultasPorDoctor(doctor.getId());

            model.addAttribute("doctor", doctor);
            model.addAttribute("totalConsultas", consultasRecientes.size());
            model.addAttribute("consultasRecientes", consultasRecientes.stream().limit(5).toList());

            return "doctor/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar dashboard: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/pendientes")
    public String listarTicketsPendientes(Model model, HttpSession session) {
        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            List<TicketAdmision> ticketsPendientes = ticketService.listarTicketsPorEstado(EstadoTicket.EN_TRIAGE);

            model.addAttribute("tickets", ticketsPendientes);
            model.addAttribute("doctor", doctor);

            return "doctor/pendientes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar tickets: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/iniciar-consulta/{ticketId}")
    public String iniciarConsulta(
            @PathVariable UUID ticketId,
            Model model,
            HttpSession session) {

        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            TicketAdmision ticket = ticketService.obtenerTicketPorId(ticketId).orElseThrow(() -> new Exception("Ticket no encontrado"));

            Consulta consulta = new Consulta();
            consulta.setTicket(ticket);
            consulta.setPaciente(ticket.getPaciente());
            consulta.setDoctor(doctor);
            consulta.setEnfermera(ticket.getEnfermera());
            consulta.setFechaHora(LocalDateTime.now());

            Consulta consultaGuardada = consultaService.crearConsulta(consulta);

            ticket.setEstado(EstadoTicket.EN_CONSULTA);
            ticketService.actualizarTicket(ticket);

            return "redirect:/doctor/consultas/" + consultaGuardada.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar consulta: " + e.getMessage());
            return "redirect:/doctor/pendientes";
        }
    }

    @GetMapping("/consultas")
    public String listarConsultas(Model model, HttpSession session) {
        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            List<Consulta> consultas = consultaService.listarConsultasPorDoctor(doctor.getId());

            model.addAttribute("consultas", consultas);
            model.addAttribute("doctor", doctor);

            return "doctor/consultas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar consultas: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/consultas/nueva")
    public String formularioNuevaConsulta(Model model, HttpSession session) {
        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            List<Paciente> pacientes = pacienteService.listarTodosLosPacientes();

            model.addAttribute("pacientes", pacientes);
            model.addAttribute("consulta", new Consulta());

            return "doctor/nueva-consulta";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/consultas/nueva")
    public String crearConsulta(
            @RequestParam UUID pacienteId,
            @RequestParam String motivo,
            Model model,
            HttpSession session) {

        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            Paciente paciente = pacienteService.obtenerPacientePorId(pacienteId).orElseThrow(() -> new Exception("Paciente no encontrado"));

            Consulta consulta = new Consulta();
            consulta.setPaciente(paciente);
            consulta.setDoctor(doctor);
            consulta.setMotivo(motivo);
            consulta.setFechaHora(LocalDateTime.now());

            Consulta consultaGuardada = consultaService.crearConsulta(consulta);

            model.addAttribute("mensaje", "Consulta creada exitosamente");
            return "redirect:/doctor/consultas/" + consultaGuardada.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear consulta: " + e.getMessage());
            return formularioNuevaConsulta(model, session);
        }
    }

    @GetMapping("/consultas/{id}")
    public String detalleConsulta(
            @PathVariable UUID id,
            Model model,
            HttpSession session) {

        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            Consulta consulta = consultaService.obtenerConsultaPorId(id)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            if (!consulta.getDoctor().getId().equals(doctor.getId())) {
                model.addAttribute("error", "No tiene permisos para ver esta consulta");
                return "redirect:/doctor/consultas";
            }

            List<Diagnostico> diagnosticos = diagnosticoService.listarDiagnosticosPorConsulta(id);
            List<Tratamiento> tratamientos = tratamientoService.listarTratamientosPorDiagnostico(id);

            model.addAttribute("consulta", consulta);
            model.addAttribute("diagnosticos", diagnosticos);
            model.addAttribute("tratamientos", tratamientos);
            model.addAttribute("nuevoDiagnostico", new Diagnostico());
            model.addAttribute("nuevoTratamiento", new Tratamiento());

            return "doctor/detalle-consulta";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar consulta: " + e.getMessage());
            return "redirect:/doctor/consultas";
        }
    }

    @PostMapping("/consultas/{id}/actualizar")
    public String actualizarConsulta(
            @PathVariable UUID id,
            @RequestParam String motivo,
            Model model,
            HttpSession session) {

        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            Consulta consulta = consultaService.obtenerConsultaPorId(id)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            // Verificar permisos
            if (!consulta.getDoctor().getId().equals(doctor.getId())) {
                model.addAttribute("error", "No tiene permisos para esta acción");
                return "redirect:/doctor/consultas";
            }

            consulta.setMotivo(motivo);
            consultaService.crearConsulta(consulta);

            model.addAttribute("mensaje", "Consulta actualizada exitosamente");
            return "redirect:/doctor/consultas/" + id;

        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar consulta: " + e.getMessage());
            return "redirect:/doctor/consultas/" + id;
        }
    }

    @PostMapping("/consultas/{id}/diagnostico")
    public String crearDiagnostico(
            @PathVariable UUID id,
            @RequestParam String descripcion,
            @RequestParam String codigoCIE10,
            Model model,
            HttpSession session) {

        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            Consulta consulta = consultaService.obtenerConsultaPorId(id)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            if (!consulta.getDoctor().getId().equals(doctor.getId())) {
                model.addAttribute("error", "No tiene permisos para esta acción");
                return "redirect:/doctor/consultas";
            }

            Diagnostico diagnostico = new Diagnostico();
            diagnostico.setConsulta(consulta);
            diagnostico.setDescripcion(descripcion);
            diagnostico.setCreadoEn(LocalDateTime.now());

            diagnosticoService.crearDiagnostico(diagnostico);

            model.addAttribute("mensaje", "Diagnóstico registrado exitosamente");
            return "redirect:/doctor/consultas/" + id;

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear diagnóstico: " + e.getMessage());
            return "redirect:/doctor/consultas/" + id;
        }
    }

    @PostMapping("/consultas/{id}/tratamiento")
    public String crearTratamiento(
            @PathVariable UUID id,
            @RequestParam UUID diagnosticoId,
            @RequestParam String indicacion,
            Model model,
            HttpSession session) {

        try {
            Doctor doctor = (Doctor) session.getAttribute("usuario");
            if (doctor == null) {
                return "redirect:/login?error=sesion";
            }

            Consulta consulta = consultaService.obtenerConsultaPorId(id)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            if (!consulta.getDoctor().getId().equals(doctor.getId())) {
                model.addAttribute("error", "No tiene permisos para esta acción");
                return "redirect:/doctor/consultas";
            }

            Diagnostico diagnostico = diagnosticoService.obtenerDiagnosticoPorId(diagnosticoId).orElseThrow(() -> new Exception("Diagnóstico no encontrado"));

            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setDiagnostico(diagnostico);
            tratamiento.setIndicacion(indicacion);
            tratamiento.setCreadoEn(LocalDateTime.now());

            tratamientoService.crearTratamiento(tratamiento);

            model.addAttribute("mensaje", "Tratamiento registrado exitosamente");
            return "redirect:/doctor/consultas/" + id;

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear tratamiento: " + e.getMessage());
            return "redirect:/doctor/consultas/" + id;
        }
    }
}