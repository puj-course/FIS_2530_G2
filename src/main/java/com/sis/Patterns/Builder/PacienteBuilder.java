package com.sis.Patterns.Builder;

import com.sis.Model.Paciente;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Model.Enum.Aseguradora;
import java.time.LocalDate;
import java.util.UUID;

public class PacienteBuilder implements IPacienteBuilder {

    private Paciente paciente;

    public PacienteBuilder() {
        this.paciente = new Paciente();
        this.paciente.setActivo(true);
        this.paciente.setEsProvisional(false);
    }

    @Override
    public IPacienteBuilder conId(UUID id) {
        this.paciente.setId(id);
        return this;
    }

    @Override
    public IPacienteBuilder conUsername(String username) {
        this.paciente.setUsername(username);
        return this;
    }

    @Override
    public IPacienteBuilder conHash(String hash) {
        this.paciente.setHash(hash);
        return this;
    }

    @Override
    public IPacienteBuilder conNombres(String nombres) {
        this.paciente.setNombres(nombres);
        return this;
    }

    @Override
    public IPacienteBuilder conApellidos(String apellidos) {
        this.paciente.setApellidos(apellidos);
        return this;
    }

    @Override
    public IPacienteBuilder conEmail(String email) {
        this.paciente.setEmail(email);
        return this;
    }

    @Override
    public IPacienteBuilder conDocumento(TipoDoc tipo, String numero) {
        this.paciente.setTipoDocumento(tipo);
        this.paciente.setNumeroDocumento(numero);
        return this;
    }

    @Override
    public IPacienteBuilder conDireccion(String direccion) {
        this.paciente.setDireccion(direccion);
        return this;
    }

    @Override
    public IPacienteBuilder conFechaNacimiento(LocalDate fecha) {
        this.paciente.setFechaNacimiento(fecha);
        return this;
    }

    @Override
    public IPacienteBuilder conSexo(String sexo) {
        this.paciente.setSexo(sexo);
        return this;
    }

    @Override
    public IPacienteBuilder conTelefono(String telefono) {
        this.paciente.setTelefono(telefono);
        return this;
    }

    @Override
    public IPacienteBuilder conSeguro(Aseguradora seguro) {
        this.paciente.setSeguro(seguro);
        return this;
    }

    @Override
    public IPacienteBuilder provisional(boolean esProvisional) {
        this.paciente.setEsProvisional(esProvisional);
        return this;
    }

    @Override
    public IPacienteBuilder activo(boolean activo) {
        this.paciente.setActivo(activo);
        return this;
    }

    @Override
    public Paciente build() {
        if (paciente.getNombres() == null || paciente.getApellidos() == null) {
            throw new IllegalStateException("Debe proporcionar nombres y apellidos");
        }
        if (paciente.getTipoDocumento() == null || paciente.getNumeroDocumento() == null) {
            throw new IllegalStateException("Debe proporcionar tipo y número de documento");
        }

        Paciente resultado = this.paciente;
        this.paciente = new Paciente();
        this.paciente.setActivo(true);
        this.paciente.setEsProvisional(false);
        return resultado;
    }
}