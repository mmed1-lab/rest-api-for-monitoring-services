package com.mmed.ws.repository;

import com.mmed.ws.model.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckRepository extends JpaRepository<Check, UUID> {

    @Query("""
            SELECT c
            FROM Check c
            WHERE c.service.id = :id
            """)
    List<Check> findHistory(@Param("id") UUID serviceId);
    List<Check> findTop20ByServiceIdOrderByCheckedAtDesc(UUID serviceId);

}
