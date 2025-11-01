package com.sis.Model;

import java.util.UUID;
import java.time.LocalDateTime;

public class Diagnostico {
    private UUID id;
    private UUID consultaId;
    private String descripcion;
    private LocalDateTime creadoEn;

    public Diagnostico() {
        this.id = UUID.randomUUID();
        this.creadoEn = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(UUID consultaId) {
        this.consultaId = consultaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
