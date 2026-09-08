package com.mmed.ws.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceDTO(UUID id,
                         String name,
                         String url,
                         String status,
                         int lastStatusCode,
                         int lastResponseTime,
                         LocalDateTime lastCheckedAt,
                         LocalDateTime createdAt,
                         UserDTO user
) {}
