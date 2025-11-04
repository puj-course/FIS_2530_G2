package com.sis.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Entity
@Table(name = "ticket_admision")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketAdmision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id",
            foreignKey = @ForeignKey(name = "fk_ticket_paciente"))
    private Usuario paciente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermera_id",
            foreignKey = @ForeignKey(name = "fk_ticket_enfermera"))
    private Usuario enfermera;

    @Column(nullable = false)
    private String eps;

    @Column(name = "fecha_hora_admision", nullable = false)
    private Instant fechaHoraAdmision;

    // ticket -> triage (delete cascade)
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Triage> triages = new ArrayList<>();
}
