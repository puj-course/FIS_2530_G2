package com.sis.Model;

import com.sis.Model.Enum.NivelTriage;
import java.util.UUID;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Triage {
    private UUID id;
    private UUID ticketId;
    private String sintomas;
    private BigDecimal temperaturaC;
    private int frecuenciaCardiaca;
    private int presionSistolica;
    private int presionDiastolica;
    private int saturacionO2;
    private NivelTriage prioridad;
    private LocalDateTime registradoEn;

    public Triage() {
        this.id = UUID.randomUUID();
        this.registradoEn = LocalDateTime.now();
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

    public int getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(int frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public int getPresionSistolica() {
        return presionSistolica;
    }

    public void setPresionSistolica(int presionSistolica) {
        this.presionSistolica = presionSistolica;
    }

    public int getPresionDiastolica() {
        return presionDiastolica;
    }

    public void setPresionDiastolica(int presionDiastolica) {
        this.presionDiastolica = presionDiastolica;
    }

    public int getSaturacionO2() {
        return saturacionO2;
    }

    public void setSaturacionO2(int saturacionO2) {
        this.saturacionO2 = saturacionO2;
    }

    public NivelTriage getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(NivelTriage prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getRegistradoEn() {
        return registradoEn;
    }

    public void setRegistradoEn(LocalDateTime registradoEn) {
        this.registradoEn = registradoEn;
    }

    public void calcularPrioridad() {
        if (temperaturaC.compareTo(new BigDecimal("39.5")) > 0 || saturacionO2 < 90 || presionSistolica > 180) {
            this.prioridad = NivelTriage.NIVEL_2_EMERGENCIA;
        } else if (temperaturaC.compareTo(new BigDecimal("38.5")) > 0 || saturacionO2 < 94) {
            this.prioridad = NivelTriage.NIVEL_3_URGENTE;
        } else {
            this.prioridad = NivelTriage.NIVEL_4_MENOS_URGENTE;
        }
    }
}
