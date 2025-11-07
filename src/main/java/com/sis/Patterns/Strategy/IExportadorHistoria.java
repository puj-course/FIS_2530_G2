package com.sis.Patterns.Strategy;

import com.sis.Model.Paciente;
import com.sis.Model.Consulta;
import java.util.List;

public interface IExportadorHistoria {

    String exportar(Paciente paciente, List<Consulta> consultas);

    String getExtension();

    String getMimeType();
}