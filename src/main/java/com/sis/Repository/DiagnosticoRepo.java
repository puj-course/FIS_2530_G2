package com.sis.Repository;

import com.sis.Model.Diagnostico;
import com.sis.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiagnosticoRepo extends JpaRepository<Diagnostico, UUID> {

    Diagnostico save(Diagnostico diagnostico);
    List<Diagnostico> findByConsulta_Id(UUID consultaId);
}