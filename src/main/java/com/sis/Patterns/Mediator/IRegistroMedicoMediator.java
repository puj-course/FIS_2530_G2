package com.sis.Patterns.Mediator;

import com.sis.Model.Consulta;
import com.sis.Model.Diagnostico;
import com.sis.Model.Tratamiento;
import com.sis.Model.Medicamento;
import com.sis.Model.DiagnosticoMedicamento;

public interface IRegistroMedicoMediator {

    Diagnostico registrarDiagnostico(Consulta consulta, String descripcion);

    Tratamiento agregarTratamiento(Diagnostico diagnostico, String indicacion);

    DiagnosticoMedicamento prescribirMedicamento(
            Diagnostico diagnostico,
            Medicamento medicamento,
            String dosis,
            String frecuencia,
            String duracion,
            String indicaciones);
}