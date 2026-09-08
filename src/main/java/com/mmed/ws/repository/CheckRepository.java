package com.mmed.ws.repository;

import com.mmed.ws.model.Check;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheckRepository extends JpaRepository<Check, UUID> {
}
