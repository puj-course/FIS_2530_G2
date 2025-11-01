package com.sis.Model;

import java.util.UUID;

public class Medicamento {
    private UUID id;
    private String nombre;
    private String forma;
    private String concentracion;

    public Medicamento() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getDescripcionCompleta() {
        return nombre + " " + forma + " " + concentracion;
    }
}
