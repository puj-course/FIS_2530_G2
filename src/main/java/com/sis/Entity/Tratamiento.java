package com.sis.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Entity
@Table(name = "tratamiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id",
            foreignKey = @ForeignKey(name = "fk_tratamiento_consulta"))
    private Consulta consulta;

    @Column(nullable = false, columnDefinition = "text")
    private String indicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id",
            foreignKey = @ForeignKey(name = "fk_tratamiento_medicamento"))
    private MedicamentoCatalogo medicamento;

    private String dosis;
    private String frecuencia;
    private String duracion;

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;
}
