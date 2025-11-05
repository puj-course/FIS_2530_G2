package com.sis.Model;

import java.util.Date;
import com.sis.Model.Enum.Aseguradora;

public class Paciente extends Usuario {
    private Date fechaNacimiento;
    private String sexo;
    private String telefono;
    private Aseguradora seguro;
    private boolean esProvisional;

    public Paciente() {
        super();
        this.esProvisional = false;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
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
        Date hoy = new Date();
        long diff = hoy.getTime() - fechaNacimiento.getTime();
        return (int) (diff / (1000L * 60 * 60 * 24 * 365));
    }
}
