package com.sis.Patterns.Strategy;

import com.sis.Model.Paciente;
import com.sis.Model.Consulta;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ExportadorHTML implements IExportadorHistoria {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public String exportar(Paciente paciente, List<Consulta> consultas) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang='es'>\n");
        html.append("<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>Historial Médico - ").append(paciente.getNombreCompleto()).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }\n");
        html.append("        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
        html.append("        h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }\n");
        html.append("        h2 { color: #34495e; margin-top: 30px; }\n");
        html.append("        .patient-info { background: #ecf0f1; padding: 15px; border-radius: 5px; margin: 20px 0; }\n");
        html.append("        .consulta { background: #fff; border-left: 4px solid #3498db; padding: 15px; margin: 15px 0; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }\n");
        html.append("        .label { font-weight: bold; color: #7f8c8d; }\n");
        html.append("        .footer { margin-top: 30px; text-align: center; color: #95a5a6; font-size: 12px; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class='container'>\n");

        // Título
        html.append("        <h1>Historial Médico</h1>\n");

        // Información del paciente
        html.append("        <div class='patient-info'>\n");
        html.append("            <h2>Datos del Paciente</h2>\n");
        html.append("            <p><span class='label'>Nombre:</span> ").append(paciente.getNombreCompleto()).append("</p>\n");
        html.append("            <p><span class='label'>Documento:</span> ").append(paciente.getTipoDocumento())
                .append(" - ").append(paciente.getNumeroDocumento()).append("</p>\n");
        html.append("            <p><span class='label'>Edad:</span> ").append(paciente.getEdad()).append(" años</p>\n");
        html.append("            <p><span class='label'>Sexo:</span> ").append(paciente.getSexo()).append("</p>\n");
        html.append("            <p><span class='label'>Seguro:</span> ").append(paciente.getSeguro()).append("</p>\n");
        html.append("            <p><span class='label'>Teléfono:</span> ").append(paciente.getTelefono()).append("</p>\n");
        html.append("            <p><span class='label'>Email:</span> ").append(paciente.getEmail()).append("</p>\n");
        html.append("        </div>\n");

        // Consultas
        html.append("        <h2>Historial de Consultas</h2>\n");

        if (consultas.isEmpty()) {
            html.append("        <p>No hay consultas registradas.</p>\n");
        } else {
            int numero = 1;
            for (Consulta consulta : consultas) {
                html.append("        <div class='consulta'>\n");
                html.append("            <h3>Consulta #").append(numero++).append("</h3>\n");
                html.append("            <p><span class='label'>Fecha:</span> ").append(consulta.getFechaHora().format(formatter)).append("</p>\n");
                html.append("            <p><span class='label'>Doctor:</span> ").append(consulta.getDoctor().getNombreCompleto()).append("</p>\n");
                html.append("            <p><span class='label'>Especialidad:</span> ").append(consulta.getDoctor().getEspecialidad()).append("</p>\n");
                html.append("            <p><span class='label'>Motivo:</span> ").append(consulta.getMotivo() != null ? consulta.getMotivo() : "N/A").append("</p>\n");
                html.append("        </div>\n");
            }
        }

        html.append("        <div class='footer'>\n");
        html.append("            <p>Documento generado automáticamente - Sistema de Información en Salud</p>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    @Override
    public String getExtension() {
        return "html";
    }

    @Override
    public String getMimeType() {
        return "text/html";
    }
}