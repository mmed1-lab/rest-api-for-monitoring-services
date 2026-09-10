package com.mmed.ws.repository;

import com.mmed.ws.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {

    Optional<Service> findByUrl(String url);

    @Query("""
            SELECT s
            FROM Service s
            WHERE s.user.email = :email
            """)
    List<Service> findUserServices(String email);
}
