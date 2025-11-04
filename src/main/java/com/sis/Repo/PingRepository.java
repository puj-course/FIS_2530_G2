// com.sis.repo.PingRepository.java
package com.sis.Repo;
import com.sis.Entity.Ping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface PingRepository extends JpaRepository<Ping, UUID> {}
