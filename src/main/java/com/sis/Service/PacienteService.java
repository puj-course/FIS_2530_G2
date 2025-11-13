package com.sis.Service;

import com.sis.Model.Paciente;
import com.sis.Repository.PacienteRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PacienteService {

    private final PacienteRepo pacienteRepository;

    public PacienteService(PacienteRepo pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente crearPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Optional<Paciente> obtenerPacientePorId(UUID pacienteId) {
        return pacienteRepository.findById(pacienteId);
    }

    public List<Paciente> listarTodosLosPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente actualizarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void eliminarPaciente(UUID pacienteId) {
        pacienteRepository.deleteById(pacienteId);
    }

    public Optional<Paciente> buscarPorDocumento(String numeroDocumento) {
        return pacienteRepository.findByNumeroDocumento(numeroDocumento);
    }
}