package com.sis.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "triage")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",
            foreignKey = @ForeignKey(name = "fk_triage_ticket"))
    private TicketAdmision ticket;

    @Column(columnDefinition = "text")
    private String sintomas;

    @Column(name = "temperatura_c", precision = 4, scale = 1)
    private BigDecimal temperaturaC;

    @Column(name = "frecuencia_cardiaca")
    private Short frecuenciaCardiaca;

    @Column(name = "presion_sistolica")
    private Short presionSistolica;

    @Column(name = "presion_diastolica")
    private Short presionDiastolica;

    @Column(name = "saturacion_o2")
    private Short saturacionO2;

    @Enumerated(EnumType.STRING)
    @Column(length = 3)
    private Prioridad prioridad;

    @CreationTimestamp
    @Column(name = "registrado_en", nullable = false, updatable = false)
    private Instant registradoEn;
}

