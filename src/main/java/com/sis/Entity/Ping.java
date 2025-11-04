package com.sis.Entity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Ping {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String msg;
}