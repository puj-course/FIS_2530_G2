package com.sis.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Entity
@Table(name = "medicamento_catalogo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MedicamentoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;
}
