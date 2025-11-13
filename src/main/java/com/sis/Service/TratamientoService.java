package com.sis.Service;

import com.sis.Model.Tratamiento;
import com.sis.Repository.TratamientoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TratamientoService {

    private final TratamientoRepo tratamientoRepository;

    public TratamientoService(TratamientoRepo tratamientoRepository) {
        this.tratamientoRepository = tratamientoRepository;
    }

    public Tratamiento crearTratamiento(Tratamiento tratamiento) {
        return tratamientoRepository.save(tratamiento);
    }

    public List<Tratamiento> listarTratamientosPorDiagnostico(UUID consultaId) {
        return tratamientoRepository.findByDiagnostico(consultaId);
    }
}
