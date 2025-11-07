package com.sis.Patterns.Prototype;

import com.sis.Model.Paciente;

public class PacientePrototype implements IPrototype<Paciente> {

    private Paciente paciente;

    public PacientePrototype(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public Paciente clone() {
        Paciente nuevoPaciente = new Paciente();

        // Datos heredados de Usuario
        nuevoPaciente.setUsername(paciente.getUsername() + "copia");
        nuevoPaciente.setHash(paciente.getHash());
        nuevoPaciente.setNombres(paciente.getNombres());
        nuevoPaciente.setApellidos(paciente.getApellidos());
        nuevoPaciente.setEmail(paciente.getEmail());
        nuevoPaciente.setTipoDocumento(paciente.getTipoDocumento());
        nuevoPaciente.setNumeroDocumento(paciente.getNumeroDocumento() + "_TEMP");
        nuevoPaciente.setDireccion(paciente.getDireccion());
        nuevoPaciente.setActivo(paciente.isActivo());

        // Datos específicos de Paciente
        // Fecha de nacimiento NO se clona (cada persona es diferente)
        nuevoPaciente.setFechaNacimiento(null);
        nuevoPaciente.setSexo(paciente.getSexo());
        nuevoPaciente.setTelefono(paciente.getTelefono());
        nuevoPaciente.setSeguro(paciente.getSeguro());
        nuevoPaciente.setEsProvisional(false); // El clon no es provisional

        return nuevoPaciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}