package com.sis.Repository;

import com.sis.Model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicamentoRepo extends JpaRepository<Medicamento, UUID> {

    Medicamento save(Medicamento medicamento);
    Optional<Medicamento> findById(UUID id);

    @Query("SELECT m FROM Medicamento m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Medicamento> findByNombre(@Param("nombre") String nombre);

    // MÉTODO CORREGIDO - Búsqueda exacta con @Query
    @Query("SELECT m FROM Medicamento m WHERE m.nombre = :nombre")
    Optional<Medicamento> findByNombreExacto(@Param("nombre") String nombre);

    @Query("SELECT m FROM Medicamento m WHERE m.forma = :forma")
    List<Medicamento> findByForma(@Param("forma") String forma);

    @Query("SELECT m FROM Medicamento m ORDER BY m.nombre ASC")
    List<Medicamento> findAllOrderByNombre();
}