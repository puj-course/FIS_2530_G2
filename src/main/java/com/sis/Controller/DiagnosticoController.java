package com.sis.Controller;

import com.sis.Model.Consulta;
import com.sis.Model.Diagnostico;
import com.sis.Model.Tratamiento;
import com.sis.Model.Medicamento;
import com.sis.Model.DiagnosticoMedicamento;
import com.sis.Model.Enum.TipoEvento;
import com.sis.Patterns.Mediator.IRegistroMedicoMediator;
import com.sis.Repository.ConsultaRepo;
import com.sis.Repository.MedicamentoRepo;
import com.sis.Repository.DiagnosticoRepo;
import com.sis.Repository.TratamientoRepo;
import com.sis.Repository.DiagMedRepo;
import com.sis.Service.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/diagnostico")
public class DiagnosticoController {

    @Autowired
    private IRegistroMedicoMediator mediator;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private ConsultaRepo consultaRepo;

    @Autowired
    private MedicamentoRepo medicamentoRepo;

    @Autowired
    private DiagnosticoRepo diagnosticoRepo;

    @Autowired
    private TratamientoRepo tratamientoRepo;

    @Autowired
    private DiagMedRepo diagMedRepo;

    @GetMapping("/registrar/{consultaId}")
    public String mostrarFormularioDiagnostico(@PathVariable UUID consultaId, Model model) {
        try {
            Consulta consulta = consultaRepo.findById(consultaId)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            model.addAttribute("consulta", consulta);
            model.addAttribute("paciente", consulta.getPaciente());

            // Cargar diagnósticos existentes
            List<Diagnostico> diagnosticos = diagnosticoRepo.findByConsulta(consultaId);
            model.addAttribute("diagnosticos", diagnosticos);

            return "diagnostico/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
            return "redirect:/consulta/pendientes";
        }
    }

    @PostMapping("/registrar")
    public String registrarDiagnostico(
            @RequestParam UUID consultaId,
            @RequestParam String descripcion,
            Model model) {

        try {
            Consulta consulta = consultaRepo.findById(consultaId)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            // Usar Mediator Pattern para registrar diagnóstico
            Diagnostico diagnostico = mediator.registrarDiagnostico(consulta, descripcion);

            // Publicar evento - YA ESTÁ EN EL MEDIATOR, no necesitas publicarlo aquí
            // El mediator ya publica DIAGNOSTICO_REGISTRADO y DIAGNOSTICO_LISTO

            model.addAttribute("mensaje", "Diagnóstico registrado exitosamente");
            model.addAttribute("diagnosticoId", diagnostico.getId());

            return "redirect:/diagnostico/detalle/" + diagnostico.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar diagnóstico: " + e.getMessage());
            return mostrarFormularioDiagnostico(consultaId, model);
        }
    }

    @GetMapping("/detalle/{diagnosticoId}")
    public String verDetalleDiagnostico(@PathVariable UUID diagnosticoId, Model model) {
        try {
            Diagnostico diagnostico = diagnosticoRepo.findById(diagnosticoId)
                    .orElseThrow(() -> new Exception("Diagnóstico no encontrado"));

            // Cargar tratamientos
            List<Tratamiento> tratamientos = tratamientoRepo.findByDiagnostico(diagnosticoId);

            // Cargar medicamentos prescritos
            List<DiagnosticoMedicamento> medicamentos = diagMedRepo.findByDiagnostico(diagnosticoId);

            model.addAttribute("diagnostico", diagnostico);
            model.addAttribute("tratamientos", tratamientos);
            model.addAttribute("medicamentos", medicamentos);

            // Lista de medicamentos disponibles para prescribir
            List<Medicamento> medicamentosDisponibles = medicamentoRepo.findAll();
            model.addAttribute("medicamentosDisponibles", medicamentosDisponibles);

            return "diagnostico/detalle";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalle: " + e.getMessage());
            return "redirect:/consulta/pendientes";
        }
    }

    @PostMapping("/tratamiento/agregar")
    public String prescribirTratamiento(
            @RequestParam UUID diagnosticoId,
            @RequestParam String indicacion,
            Model model) {

        try {
            Diagnostico diagnostico = diagnosticoRepo.findById(diagnosticoId)
                    .orElseThrow(() -> new Exception("Diagnóstico no encontrado"));

            // Usar Mediator Pattern para agregar tratamiento
            Tratamiento tratamiento = mediator.agregarTratamiento(diagnostico, indicacion);

            // YA NO NECESITAS PUBLICAR - El mediator lo hace automáticamente

            model.addAttribute("mensaje", "Tratamiento agregado exitosamente");
            return "redirect:/diagnostico/detalle/" + diagnosticoId;

        } catch (Exception e) {
            model.addAttribute("error", "Error al agregar tratamiento: " + e.getMessage());
            return verDetalleDiagnostico(diagnosticoId, model);
        }
    }

    @PostMapping("/medicamento/prescribir")
    public String prescribirMedicamento(
            @RequestParam UUID diagnosticoId,
            @RequestParam UUID medicamentoId,
            @RequestParam String dosis,
            @RequestParam String frecuencia,
            @RequestParam String duracion,
            @RequestParam(required = false) String indicaciones,
            Model model) {

        try {
            Diagnostico diagnostico = diagnosticoRepo.findById(diagnosticoId)
                    .orElseThrow(() -> new Exception("Diagnóstico no encontrado"));

            Medicamento medicamento = medicamentoRepo.findById(medicamentoId)
                    .orElseThrow(() -> new Exception("Medicamento no encontrado"));

            // Usar Mediator Pattern para prescribir medicamento
            DiagnosticoMedicamento diagMedicamento = mediator.prescribirMedicamento(
                    diagnostico, medicamento, dosis, frecuencia, duracion, indicaciones);

            // YA NO NECESITAS PUBLICAR - El mediator lo hace automáticamente

            model.addAttribute("mensaje", "Medicamento prescrito exitosamente");
            return "redirect:/diagnostico/detalle/" + diagnosticoId;

        } catch (Exception e) {
            model.addAttribute("error", "Error al prescribir medicamento: " + e.getMessage());
            return verDetalleDiagnostico(diagnosticoId, model);
        }
    }

    @PostMapping("/medicamento/eliminar/{diagMedicamentoId}")
    public String eliminarMedicamento(
            @PathVariable UUID diagMedicamentoId,
            @RequestParam UUID diagnosticoId,
            Model model) {

        try {
            diagMedRepo.deleteById(diagMedicamentoId);

            model.addAttribute("mensaje", "Medicamento eliminado de la prescripción");
            return "redirect:/diagnostico/detalle/" + diagnosticoId;

        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar medicamento: " + e.getMessage());
            return "redirect:/diagnostico/detalle/" + diagnosticoId;
        }
    }

    @GetMapping("/buscar-medicamento")
    @ResponseBody
    public List<Medicamento> buscarMedicamento(@RequestParam String nombre) {
        try {
            return medicamentoRepo.findByNombre(nombre);
        } catch (Exception e) {
            return List.of();
        }
    }

    @PostMapping("/actualizar")
    public String actualizarDiagnostico(
            @RequestParam UUID diagnosticoId,
            @RequestParam String descripcion,
            Model model) {

        try {
            Diagnostico diagnostico = diagnosticoRepo.findById(diagnosticoId)
                    .orElseThrow(() -> new Exception("Diagnóstico no encontrado"));

            diagnostico.setDescripcion(descripcion);
            diagnosticoRepo.save(diagnostico);

            // Publicar evento - CORREGIDO: Usar TipoEvento
            eventBus.publicar(TipoEvento.DIAGNOSTICO_ACTUALIZADO, diagnostico);

            model.addAttribute("mensaje", "Diagnóstico actualizado exitosamente");
            return "redirect:/diagnostico/detalle/" + diagnosticoId;

        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar diagnóstico: " + e.getMessage());
            return verDetalleDiagnostico(diagnosticoId, model);
        }
    }

    @GetMapping("/consulta/{consultaId}/lista")
    public String listarDiagnosticosPorConsulta(@PathVariable UUID consultaId, Model model) {
        try {
            Consulta consulta = consultaRepo.findById(consultaId)
                    .orElseThrow(() -> new Exception("Consulta no encontrada"));

            List<Diagnostico> diagnosticos = diagnosticoRepo.findByConsulta(consultaId);

            model.addAttribute("consulta", consulta);
            model.addAttribute("diagnosticos", diagnosticos);

            return "diagnostico/lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al listar diagnósticos: " + e.getMessage());
            return "redirect:/consulta/pendientes";
        }
    }
}