package com.sis.Repository;

import com.sis.Model.Diagnostico;
import com.sis.Model.DiagnosticoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiagMedRepo extends JpaRepository<DiagnosticoMedicamento, UUID> {

    DiagnosticoMedicamento save(DiagnosticoMedicamento diagnosticoMedicamento);
    List<DiagnosticoMedicamento> findByDiagnostico(UUID diagnosticoId);
}