package com.sis.DTO;

import com.sis.Model.Enum.NivelTriage;
import java.math.BigDecimal;
import java.util.UUID;

public class TriageFormDTO {
    private UUID ticketId;
    private String sintomas;
    private BigDecimal temperaturaC;
    private Integer frecuenciaCardiaca;
    private Integer presionSistolica;
    private Integer presionDiastolica;
    private Integer saturacionO2;
    private NivelTriage prioridad;

    public TriageFormDTO() {
    }

    public TriageFormDTO(UUID ticketId, String sintomas, BigDecimal temperaturaC,
                         Integer frecuenciaCardiaca, Integer presionSistolica,
                         Integer presionDiastolica, Integer saturacionO2, NivelTriage prioridad) {
        this.ticketId = ticketId;
        this.sintomas = sintomas;
        this.temperaturaC = temperaturaC;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.presionSistolica = presionSistolica;
        this.presionDiastolica = presionDiastolica;
        this.saturacionO2 = saturacionO2;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public BigDecimal getTemperaturaC() {
        return temperaturaC;
    }

    public void setTemperaturaC(BigDecimal temperaturaC) {
        this.temperaturaC = temperaturaC;
    }

    public Integer getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Integer getPresionSistolica() {
        return presionSistolica;
    }

    public void setPresionSistolica(Integer presionSistolica) {
        this.presionSistolica = presionSistolica;
    }

    public Integer getPresionDiastolica() {
        return presionDiastolica;
    }

    public void setPresionDiastolica(Integer presionDiastolica) {
        this.presionDiastolica = presionDiastolica;
    }

    public Integer getSaturacionO2() {
        return saturacionO2;
    }

    public void setSaturacionO2(Integer saturacionO2) {
        this.saturacionO2 = saturacionO2;
    }

    public NivelTriage getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(NivelTriage prioridad) {
        this.prioridad = prioridad;
    }
}