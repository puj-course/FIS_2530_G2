package com.sis.Model;

import java.util.UUID;
import java.time.LocalDateTime;

public class Consulta {
    private UUID id;
    private UUID ticketId;
    private UUID pacienteId;
    private UUID doctorId;
    private UUID enfermeraId;
    private LocalDateTime fechaHora;
    private String motivo;

    public Consulta() {
        this.id = UUID.randomUUID();
        this.fechaHora = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(UUID pacienteId) {
        this.pacienteId = pacienteId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getEnfermeraId() {
        return enfermeraId;
    }

    public void setEnfermeraId(UUID enfermeraId) {
        this.enfermeraId = enfermeraId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
