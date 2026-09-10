package com.mmed.ws.tool;

import com.mmed.ws.dto.CheckDTO;
import com.mmed.ws.dto.ServiceDTO;
import com.mmed.ws.dto.UserDTO;
import com.mmed.ws.model.Service;
import com.mmed.ws.repository.CheckRepository;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;
import java.util.UUID;

public record HistoryToolDefault(CheckRepository repository) {

    @Tool(description = "Get the checking history of a service")
    public List<CheckDTO> getLastCheckServiceHistory(UUID serviceId) {
        return repository
                .findTop20ByServiceIdOrderByCheckedAtDesc(serviceId)
                .stream()
                .map(check -> {
                    Service service1 = check.getService();
                    ServiceDTO serviceDto = new ServiceDTO(
                            service1.getId(), service1.getName(), service1.getUrl(), service1.getStatus(),
                            service1.getLastStatusCode(), service1.getLastResponseTime(), service1.getLastCheckedAt(),
                            service1.getCreatedAt(), new UserDTO(service1.getUser().getId(), service1.getUser().getEmail(),
                            service1.getUser().getCreatedAt())
                    );
                    return new CheckDTO(
                            check.getId(),check.getStatus(), check.getStatusCode(),
                            check.getResponseTime(), check.getCheckedAt(), serviceDto
                    );
                }).toList();
    }
}
