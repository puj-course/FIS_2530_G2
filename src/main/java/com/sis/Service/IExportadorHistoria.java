package com.sis.Service;

import com.sis.Service.HistorialMedico;
import org.springframework.stereotype.Service;

@Service
public interface IExportadorHistoria {
    void exportar(HistorialMedico historial);
}