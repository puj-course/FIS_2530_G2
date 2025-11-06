package com.sis.Patterns.Builder;

import com.sis.Model.Paciente;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Model.Enum.Aseguradora;
import java.time.LocalDate;
import java.util.UUID;


public interface IPacienteBuilder {

    IPacienteBuilder conId(UUID id);
    IPacienteBuilder conUsername(String username);
    IPacienteBuilder conHash(String hash);
    IPacienteBuilder conNombres(String nombres);
    IPacienteBuilder conApellidos(String apellidos);
    IPacienteBuilder conEmail(String email);
    IPacienteBuilder conDocumento(TipoDoc tipo, String numero);
    IPacienteBuilder conDireccion(String direccion);
    IPacienteBuilder conFechaNacimiento(LocalDate fecha);
    IPacienteBuilder conSexo(String sexo);
    IPacienteBuilder conTelefono(String telefono);
    IPacienteBuilder conSeguro(Aseguradora seguro);
    IPacienteBuilder provisional(boolean esProvisional);
    IPacienteBuilder activo(boolean activo);

    Paciente build();
}