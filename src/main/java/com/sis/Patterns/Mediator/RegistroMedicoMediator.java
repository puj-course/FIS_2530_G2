package com.sis.Patterns.Mediator;

import com.sis.Model.*;
        import com.sis.Model.Enum.TipoEvento;
import com.sis.Repository.DiagnosticoRepo;
import com.sis.Repository.TratamientoRepo;
import com.sis.Repository.DiagMedRepo;
import com.sis.Service.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistroMedicoMediator implements IRegistroMedicoMediator {

    @Autowired
    private DiagnosticoRepo diagnosticoRepo;

    @Autowired
    private TratamientoRepo tratamientoRepo;

    @Autowired
    private DiagMedRepo diagMedicamentoRepo;

    @Autowired
    private EventBus eventBus;

    @Override
    public Diagnostico registrarDiagnostico(Consulta consulta, String descripcion) {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setConsulta(consulta);
        diagnostico.setDescripcion(descripcion);

        Diagnostico guardado = diagnosticoRepo.save(diagnostico);

        // Notificar usando TipoEvento
        eventBus.publicar(TipoEvento.DIAGNOSTICO_REGISTRADO, guardado);
        eventBus.publicar(TipoEvento.DIAGNOSTICO_LISTO,
                "Diagnóstico para paciente: " + consulta.getPaciente().getNombreCompleto());

        return guardado;
    }

    @Override
    public Tratamiento agregarTratamiento(Diagnostico diagnostico, String indicacion) {
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setDiagnostico(diagnostico);
        tratamiento.setIndicacion(indicacion);

        Tratamiento guardado = tratamientoRepo.save(tratamiento);

        // Notificar
        eventBus.publicar(TipoEvento.TRATAMIENTO_AGREGADO, guardado);

        return guardado;
    }

    @Override
    public DiagnosticoMedicamento prescribirMedicamento(
            Diagnostico diagnostico,
            Medicamento medicamento,
            String dosis,
            String frecuencia,
            String duracion,
            String indicaciones) {

        DiagnosticoMedicamento diagMedicamento = new DiagnosticoMedicamento();
        diagMedicamento.setDiagnostico(diagnostico);
        diagMedicamento.setMedicamento(medicamento);
        diagMedicamento.setDosis(dosis);
        diagMedicamento.setFrecuencia(frecuencia);
        diagMedicamento.setDuracion(duracion);
        diagMedicamento.setIndicaciones(indicaciones);

        DiagnosticoMedicamento guardado = diagMedicamentoRepo.save(diagMedicamento);

        // Notificar
        eventBus.publicar(TipoEvento.MEDICAMENTO_PRESCRITO,
                medicamento.getNombre() + " - " + dosis + " " + frecuencia);

        return guardado;
    }
}