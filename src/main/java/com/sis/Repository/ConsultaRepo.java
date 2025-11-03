package com.sis.Repository;

import com.sis.Model.Consulta;
import com.sis.Model.Doctor;
import com.sis.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultaRepo extends JpaRepository<Consulta, UUID> {

    Consulta save(Consulta consulta);
    List<Consulta> findByConsulta(UUID consultaId);
    List<Consulta> findByDoctor(UUID doctorId);
}