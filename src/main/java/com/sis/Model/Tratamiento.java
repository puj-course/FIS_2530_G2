package com.sis.Model;

import java.util.UUID;
import java.time.LocalDateTime;

public class Tratamiento {
    private UUID id;
    private UUID diagnosticoId;
    private String indicacion;
    private LocalDateTime creadoEn;

    public Tratamiento() {
        this.id = UUID.randomUUID();
        this.creadoEn = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDiagnosticoId() {
        return diagnosticoId;
    }

    public void setDiagnosticoId(UUID diagnosticoId) {
        this.diagnosticoId = diagnosticoId;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
