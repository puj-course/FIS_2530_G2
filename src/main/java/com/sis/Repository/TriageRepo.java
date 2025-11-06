package com.sis.Repository;

import com.sis.Model.Triage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TriageRepo extends JpaRepository<Triage, UUID> {

    Triage save(Triage triage);
    Optional<Triage> findByTicket(UUID ticketId);
}