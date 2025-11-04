package com.sis.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Entity
@Table(name = "diagnostico")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id",
            foreignKey = @ForeignKey(name = "fk_diagnostico_consulta"))
    private Consulta consulta;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false)
    private Boolean confirmado = true;

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;
}
