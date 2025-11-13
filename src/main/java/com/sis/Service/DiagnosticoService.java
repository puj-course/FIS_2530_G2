package com.sis.Service;

import com.sis.Model.Diagnostico;
import com.sis.Repository.DiagnosticoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiagnosticoService {

    private final DiagnosticoRepo diagnosticoRepository;

    public DiagnosticoService(DiagnosticoRepo diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }

    public Diagnostico crearDiagnostico(Diagnostico diagnostico) {
        return diagnosticoRepository.save(diagnostico);
    }

    public List<Diagnostico> listarDiagnosticosPorConsulta(UUID consultaId) {
        return diagnosticoRepository.findByConsulta(consultaId);
    }

    // En DiagnosticoService.java
    public Optional<Diagnostico> obtenerDiagnosticoPorId(UUID diagnosticoId) {
        return diagnosticoRepository.findById(diagnosticoId);
    }
}
