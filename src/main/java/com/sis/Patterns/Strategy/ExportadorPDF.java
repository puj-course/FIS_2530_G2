package com.sis.Patterns.Strategy;

import com.sis.Model.Paciente;
import com.sis.Model.Consulta;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ExportadorPDF implements IExportadorHistoria {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public String exportar(Paciente paciente, List<Consulta> consultas) {
        // En producción, esto generaría un PDF real
        // Por ahora retornamos una representación en texto
        StringBuilder pdf = new StringBuilder();

        pdf.append("==========================================\n");
        pdf.append("       HISTORIAL MÉDICO - PDF\n");
        pdf.append("==========================================\n\n");

        // Información del paciente
        pdf.append("DATOS DEL PACIENTE\n");
        pdf.append("------------------------------------------\n");
        pdf.append("Nombre: ").append(paciente.getNombreCompleto()).append("\n");
        pdf.append("Documento: ").append(paciente.getTipoDocumento())
                .append(" - ").append(paciente.getNumeroDocumento()).append("\n");
        pdf.append("Edad: ").append(paciente.getEdad()).append(" años\n");
        pdf.append("Sexo: ").append(paciente.getSexo()).append("\n");
        pdf.append("Seguro: ").append(paciente.getSeguro()).append("\n");
        pdf.append("Teléfono: ").append(paciente.getTelefono()).append("\n");
        pdf.append("Email: ").append(paciente.getEmail()).append("\n");
        pdf.append("\n");

        // Consultas
        pdf.append("HISTORIAL DE CONSULTAS\n");
        pdf.append("==========================================\n\n");

        if (consultas.isEmpty()) {
            pdf.append("No hay consultas registradas.\n");
        } else {
            int numero = 1;
            for (Consulta consulta : consultas) {
                pdf.append("CONSULTA #").append(numero++).append("\n");
                pdf.append("------------------------------------------\n");
                pdf.append("Fecha: ").append(consulta.getFechaHora().format(formatter)).append("\n");
                pdf.append("Doctor: ").append(consulta.getDoctor().getNombreCompleto()).append("\n");
                pdf.append("Especialidad: ").append(consulta.getDoctor().getEspecialidad()).append("\n");
                pdf.append("Motivo: ").append(consulta.getMotivo() != null ? consulta.getMotivo() : "N/A").append("\n");
                pdf.append("\n");
            }
        }

        pdf.append("==========================================\n");
        pdf.append("Documento generado en formato PDF\n");
        pdf.append("==========================================\n");

        return pdf.toString();
    }

    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }
}