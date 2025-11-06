package com.sis.Model;

import com.sis.Model.Enum.NivelTriage;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "triages")
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private TicketAdmision ticket;

    @Column(columnDefinition = "TEXT")
    private String sintomas;

    @Column(name = "temperatura_c", precision = 4, scale = 2)
    private BigDecimal temperaturaC;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "presion_sistolica")
    private Integer presionSistolica;

    @Column(name = "presion_diastolica")
    private Integer presionDiastolica;

    @Column(name = "saturacion_o2")
    private Integer saturacionO2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelTriage prioridad;

    @Column(name = "registrado_en", nullable = false, updatable = false)
    private LocalDateTime registradoEn;

    @PrePersist
    protected void onCreate() {
        if (registradoEn == null) {
            registradoEn = LocalDateTime.now();
        }
    }

    public Triage() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TicketAdmision getTicket() {
        return ticket;
    }

    public void setTicket(TicketAdmision ticket) {
        this.ticket = ticket;
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

    public LocalDateTime getRegistradoEn() {
        return registradoEn;
    }

    public void setRegistradoEn(LocalDateTime registradoEn) {
        this.registradoEn = registradoEn;
    }

    public void calcularPrioridad() {
        if (temperaturaC != null && saturacionO2 != null && presionSistolica != null) {
            if (temperaturaC.compareTo(new BigDecimal("39.5")) > 0 ||
                    saturacionO2 < 90 ||
                    presionSistolica > 180) {
                this.prioridad = NivelTriage.NIVEL_2_EMERGENCIA;
            } else if (temperaturaC.compareTo(new BigDecimal("38.5")) > 0 ||
                    saturacionO2 < 94) {
                this.prioridad = NivelTriage.NIVEL_3_URGENTE;
            } else {
                this.prioridad = NivelTriage.NIVEL_4_MENOS_URGENTE;
            }
        }
    }
}