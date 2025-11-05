package com.sis.Repository;

import com.sis.Model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicamentoRepo extends JpaRepository<Medicamento, UUID> {

    Medicamento save(Medicamento medicamento);
    List<Medicamento> findByNombre(String nombre);
}