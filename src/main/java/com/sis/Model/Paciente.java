package com.sis.Model;

import com.sis.Model.Enum.Aseguradora;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "pacientes")
public class Paciente extends Usuario {

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 10)
    private String sexo;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "seguro")
    private Aseguradora seguro;

    @Column(name = "es_provisional")
    private boolean esProvisional = false;

    public Paciente() {
        super();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Aseguradora getSeguro() {
        return seguro;
    }

    public void setSeguro(Aseguradora seguro) {
        this.seguro = seguro;
    }

    public boolean isEsProvisional() {
        return esProvisional;
    }

    public void setEsProvisional(boolean esProvisional) {
        this.esProvisional = esProvisional;
    }

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}