package com.sis.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Component
@Entity
public class Usuario {
    @Id
    private String id;
    private String nombre;
    private String apellido;
}
