package com.sis.Model;

import java.util.UUID;
import java.time.LocalDateTime;

public class DiagnosticoMedicamento {
    private UUID id;
    private UUID diagnosticoId;
    private UUID medicamentoId;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;
    private LocalDateTime creadoEn;

    public DiagnosticoMedicamento() {
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

    public UUID getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(UUID medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
