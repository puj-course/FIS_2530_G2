package com.sis.Repository;

import com.sis.Model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepo extends JpaRepository<Paciente, UUID> {

    Optional<Paciente>findById(UUID id);
    Paciente save(Paciente paciente);
    List<Paciente> findAll();
}