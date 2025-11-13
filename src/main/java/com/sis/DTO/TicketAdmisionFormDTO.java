package com.sis.DTO;

import java.util.UUID;

public class TicketAdmisionFormDTO {
    private UUID pacienteId;
    private String observaciones;

    public TicketAdmisionFormDTO() {
    }

    public TicketAdmisionFormDTO(UUID pacienteId, String observaciones) {
        this.pacienteId = pacienteId;
        this.observaciones = observaciones;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(UUID pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}