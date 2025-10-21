package com.sis.Interface;

import com.sis.Model.Diagnostico;
import com.sis.Model.Tratamiento;
import com.sis.Model.DiagnosticoMedicamento;

public interface IRegistroMedicoMediator {

    // Registrar un diagnóstico para una consulta, se hace dentro de RegistroMedicoMediator

    Diagnostico registrarDiagnostico(String consultaId, String descripcion);


    //Agregar un tratamiento a un diagnóstico, se hace dentro de RegistroMedicoMediator

    Tratamiento agregarTratamiento(String diagnosticoId, String descripcion);


    //Prescribir un medicamento asociado a un diagnóstico

    DiagnosticoMedicamento prescribirMedicamento(String diagnosticoId, String medicamentoId, String dosis, String frecuencia, String duracion);
}