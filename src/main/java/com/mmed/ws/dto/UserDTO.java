package com.mmed.ws.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(UUID id, String email, LocalDateTime createdAt) {}
