package com.sis.Service;

import com.sis.Model.Consulta;
import com.sis.Repository.ConsultaRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultaService {

    private final ConsultaRepo consultaRepository;

    public ConsultaService(ConsultaRepo consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> listarConsultasPorDoctor(UUID doctorId) {
        return consultaRepository.findByDoctor(doctorId);
    }

    public Consulta crearConsulta(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Optional<Consulta> obtenerConsultaPorId(UUID consultaId) {
        return consultaRepository.findById(consultaId);
    }


    public List<Consulta> listarConsultasPorPaciente(UUID pacienteId) {
        return consultaRepository.findByPaciente(pacienteId);
    }
}