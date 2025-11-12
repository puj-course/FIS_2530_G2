package com.sis.Controller;

import com.sis.Model.*;
import com.sis.Model.Enum.*;
import com.sis.Repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ConsultaRepo consultaRepo;

    @Autowired
    private PacienteRepo pacienteRepo;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private DiagnosticoRepo diagnosticoRepo;

    @Autowired
    private MedicamentoRepo medicamentoRepo;

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        try {
            // Estadísticas generales
            long totalUsuarios = usuarioRepo.count();
            long totalPacientes = pacienteRepo.count();
            long totalConsultas = consultaRepo.count();
            long ticketsPendientes = ticketRepo.findByEstado(EstadoTicket.PENDIENTE).size();

            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("totalPacientes", totalPacientes);
            model.addAttribute("totalConsultas", totalConsultas);
            model.addAttribute("ticketsPendientes", ticketsPendientes);

            return "admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar dashboard: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/usuarios")
    public String gestionarUsuarios(
            @RequestParam(required = false) String tipo,
            Model model) {

        try {
            List<Usuario> usuarios;

            if (tipo != null && !tipo.isEmpty()) {
                // Filtrar por tipo de usuario
                usuarios = usuarioRepo.findAll().stream()
                        .filter(u -> u.getClass().getSimpleName().equalsIgnoreCase(tipo))
                        .collect(Collectors.toList());
            } else {
                usuarios = usuarioRepo.findAll();
            }

            model.addAttribute("usuarios", usuarios);
            model.addAttribute("tiposFiltro", TipoUsuario.values());

            return "admin/usuarios/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar usuarios: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/usuarios/detalle/{usuarioId}")
    public String verDetalleUsuario(@PathVariable UUID usuarioId, Model model) {
        try {
            Usuario usuario = usuarioRepo.findById(usuarioId)
                    .orElseThrow(() -> new Exception("Usuario no encontrado"));

            model.addAttribute("usuario", usuario);

            // Si es paciente, cargar su historial
            if (usuario instanceof Paciente) {
                List<Consulta> consultas = consultaRepo.findByPaciente(usuarioId);
                model.addAttribute("consultas", consultas);
            }

            // Si es doctor, cargar sus consultas
            if (usuario instanceof Doctor) {
                List<Consulta> consultas = consultaRepo.findByDoctor(usuarioId);
                model.addAttribute("consultas", consultas);
            }

            return "admin/usuarios/detalle";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalle: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/usuarios/activar/{usuarioId}")
    public String activarUsuario(@PathVariable UUID usuarioId, Model model) {
        try {
            Usuario usuario = usuarioRepo.findById(usuarioId)
                    .orElseThrow(() -> new Exception("Usuario no encontrado"));

            usuario.setActivo(true);
            usuarioRepo.save(usuario);

            model.addAttribute("mensaje", "Usuario activado exitosamente");
            return "redirect:/admin/usuarios/detalle/" + usuarioId;
        } catch (Exception e) {
            model.addAttribute("error", "Error al activar usuario: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/usuarios/desactivar/{usuarioId}")
    public String desactivarUsuario(@PathVariable UUID usuarioId, Model model) {
        try {
            Usuario usuario = usuarioRepo.findById(usuarioId)
                    .orElseThrow(() -> new Exception("Usuario no encontrado"));

            usuario.setActivo(false);
            usuarioRepo.save(usuario);

            model.addAttribute("mensaje", "Usuario desactivado exitosamente");
            return "redirect:/admin/usuarios/detalle/" + usuarioId;
        } catch (Exception e) {
            model.addAttribute("error", "Error al desactivar usuario: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/reportes")
    public String mostrarReportes(Model model) {
        return "admin/reportes/menu";
    }

    @GetMapping("/reportes/consultas")
    public String generarReporteConsultas(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            Model model) {

        try {
            List<Consulta> consultas;

            if (fechaInicio != null && fechaFin != null) {
                LocalDateTime inicio = fechaInicio.atStartOfDay();
                LocalDateTime fin = fechaFin.atTime(23, 59, 59);
                consultas = consultaRepo.findByFechaHoraBetween(inicio, fin);
            } else {
                consultas = consultaRepo.findAll();
            }

            // Estadísticas
            long totalConsultas = consultas.size();

            Map<String, Long> consultasPorEspecialidad = consultas.stream()
                    .collect(Collectors.groupingBy(
                            c -> c.getDoctor().getEspecialidad().toString(),
                            Collectors.counting()
                    ));

            model.addAttribute("consultas", consultas);
            model.addAttribute("totalConsultas", totalConsultas);
            model.addAttribute("consultasPorEspecialidad", consultasPorEspecialidad);
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);

            return "admin/reportes/consultas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar reporte: " + e.getMessage());
            return "redirect:/admin/reportes";
        }
    }

    @GetMapping("/reportes/pacientes")
    public String generarReportePacientes(Model model) {
        try {
            List<Paciente> pacientes = pacienteRepo.findAll();

            long totalPacientes = pacientes.size();
            long pacientesProvisionales = pacientes.stream()
                    .filter(Paciente::isEsProvisional)
                    .count();

            Map<String, Long> pacientesPorSeguro = pacientes.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getSeguro() != null ? p.getSeguro().toString() : "Sin seguro",
                            Collectors.counting()
                    ));

            Map<String, Long> pacientesPorSexo = pacientes.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getSexo() != null ? p.getSexo() : "No especificado",
                            Collectors.counting()
                    ));

            model.addAttribute("totalPacientes", totalPacientes);
            model.addAttribute("pacientesProvisionales", pacientesProvisionales);
            model.addAttribute("pacientesPorSeguro", pacientesPorSeguro);
            model.addAttribute("pacientesPorSexo", pacientesPorSexo);

            return "admin/reportes/pacientes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar reporte: " + e.getMessage());
            return "redirect:/admin/reportes";
        }
    }

    @GetMapping("/reportes/medicamentos")
    public String generarReporteMedicamentos(Model model) {
        try {
            List<Medicamento> medicamentos = medicamentoRepo.findAll();

            // Medicamentos más prescritos
            List<Map.Entry<String, Long>> medicamentosMasPrescritos =
                    medicamentos.stream()
                            .collect(Collectors.groupingBy(
                                    Medicamento::getNombre,
                                    Collectors.counting()
                            ))
                            .entrySet()
                            .stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                            .limit(10)
                            .collect(Collectors.toList());

            model.addAttribute("totalMedicamentos", medicamentos.size());
            model.addAttribute("medicamentosMasPrescritos", medicamentosMasPrescritos);

            return "admin/reportes/medicamentos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar reporte: " + e.getMessage());
            return "redirect:/admin/reportes";
        }
    }

    @GetMapping("/reportes/exportar")
    public void exportarReporte(
            @RequestParam String tipo,
            @RequestParam(defaultValue = "csv") String formato,
            HttpServletResponse response) throws IOException {

        try {
            StringBuilder csv = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            switch (tipo) {
                case "consultas":
                    List<Consulta> consultas = consultaRepo.findAll();
                    csv.append("Fecha,Paciente,Doctor,Especialidad,Motivo\n");
                    for (Consulta c : consultas) {
                        csv.append(c.getFechaHora().format(formatter)).append(",")
                                .append(c.getPaciente().getNombreCompleto()).append(",")
                                .append(c.getDoctor().getNombreCompleto()).append(",")
                                .append(c.getDoctor().getEspecialidad()).append(",")
                                .append(c.getMotivo() != null ? c.getMotivo() : "N/A").append("\n");
                    }
                    break;

                case "pacientes":
                    List<Paciente> pacientes = pacienteRepo.findAll();
                    csv.append("Documento,Nombre,Edad,Sexo,Seguro,Teléfono\n");
                    for (Paciente p : pacientes) {
                        csv.append(p.getNumeroDocumento()).append(",")
                                .append(p.getNombreCompleto()).append(",")
                                .append(p.getEdad()).append(",")
                                .append(p.getSexo() != null ? p.getSexo() : "N/A").append(",")
                                .append(p.getSeguro() != null ? p.getSeguro() : "N/A").append(",")
                                .append(p.getTelefono() != null ? p.getTelefono() : "N/A").append("\n");
                    }
                    break;

                default:
                    csv.append("Tipo de reporte no válido\n");
            }

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_" + tipo + ".csv");
            response.getWriter().write(csv.toString());
            response.getWriter().flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al exportar: " + e.getMessage());
        }
    }

    @GetMapping("/medicamentos")
    public String gestionarMedicamentos(Model model) {
        try {
            List<Medicamento> medicamentos = medicamentoRepo.findAll();
            model.addAttribute("medicamentos", medicamentos);
            return "admin/medicamentos/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar medicamentos: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/medicamentos/crear")
    public String crearMedicamento(
            @RequestParam String nombre,
            @RequestParam String forma,
            @RequestParam String concentracion,
            Model model) {

        try {
            Medicamento medicamento = new Medicamento();
            medicamento.setNombre(nombre);
            medicamento.setForma(forma);
            medicamento.setConcentracion(concentracion);

            medicamentoRepo.save(medicamento);

            model.addAttribute("mensaje", "Medicamento creado exitosamente");
            return "redirect:/admin/medicamentos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear medicamento: " + e.getMessage());
            return "redirect:/admin/medicamentos";
        }
    }

    @GetMapping("/estadisticas")
    @ResponseBody
    public String obtenerEstadisticas() {
        try {
            long totalUsuarios = usuarioRepo.count();
            long totalConsultas = consultaRepo.count();
            long consultasHoy = consultaRepo.findByFechaHoraBetween(
                    LocalDate.now().atStartOfDay(),
                    LocalDateTime.now()
            ).size();

            return String.format(
                    "{\"totalUsuarios\": %d, \"totalConsultas\": %d, \"consultasHoy\": %d}",
                    totalUsuarios, totalConsultas, consultasHoy
            );
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}