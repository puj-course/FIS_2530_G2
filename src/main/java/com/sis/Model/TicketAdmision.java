package com.sis.Model;

import com.sis.Model.Enum.EstadoTicket;
import java.util.UUID;
import java.time.LocalDateTime;

public class TicketAdmision {
    private UUID id;
    private UUID pacienteId;
    private UUID enfermeraId;
    private EstadoTicket estado;
    private LocalDateTime creadoEn;

    public TicketAdmision() {
        this.id = UUID.randomUUID();
        this.estado = EstadoTicket.PENDIENTE;
        this.creadoEn = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(UUID pacienteId) {
        this.pacienteId = pacienteId;
    }

    public UUID getEnfermeraId() {
        return enfermeraId;
    }

    public void setEnfermeraId(UUID enfermeraId) {
        this.enfermeraId = enfermeraId;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
