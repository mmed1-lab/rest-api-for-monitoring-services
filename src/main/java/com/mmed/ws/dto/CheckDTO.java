package com.mmed.ws.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CheckDTO(
        UUID id,
        String status,
        int statusCode,
        int responseTime,
        LocalDateTime checkedAt,
        ServiceDTO service
){}
