package com.sis.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Table(name = "consulta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id",
            foreignKey = @ForeignKey(name = "fk_consulta_paciente"))
    private Usuario paciente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id",
            foreignKey = @ForeignKey(name = "fk_consulta_doctor"))
    private Usuario doctor;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private Instant fechaHora;

    @Column(columnDefinition = "text")
    private String motivo;

    // consulta -> diagnosticos (cascade delete)
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Diagnostico> diagnosticos = new ArrayList<>();

    // consulta -> tratamientos (cascade delete)
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Tratamiento> tratamientos = new ArrayList<>();
}
