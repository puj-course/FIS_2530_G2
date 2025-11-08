package com.sis.Repository;

import com.sis.Model.Diagnostico;
import com.sis.Model.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TratamientoRepo extends JpaRepository<Tratamiento, UUID> {

    Tratamiento save(Tratamiento tratamiento);
    List<Tratamiento> findByDiagnostico_Id(UUID diagnosticoId);

}