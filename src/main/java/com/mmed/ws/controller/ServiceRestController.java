package com.mmed.ws.controller;

import com.mmed.ws.dto.CheckDTO;
import com.mmed.ws.dto.RestApiResponse;
import com.mmed.ws.dto.ServiceDTO;
import com.mmed.ws.dto.UserDTO;
import com.mmed.ws.exception.IDNotFoundException;
import com.mmed.ws.exception.UniqueUrlException;
import com.mmed.ws.model.Check;
import com.mmed.ws.model.Service;
import com.mmed.ws.service.MonitoringService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
public class ServiceRestController {

    private final MonitoringService service;

    public ServiceRestController(MonitoringService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Service service) {
        try {
            Service service1 = this.service.addService(service);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new RestApiResponse<>(true, "Service created Successfully", service1));
        } catch (UniqueUrlException e) {
            return new ResponseEntity<>(
                    new RestApiResponse<>(false, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getServices(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        List<ServiceDTO> services = service
                .getAllServices(email).stream()
                .map(service1 -> new ServiceDTO(
                        service1.getId(), service1.getName(), service1.getUrl(), service1.getStatus(),
                        service1.getLastStatusCode(), service1.getLastResponseTime(), service1.getLastCheckedAt(),
                        service1.getCreatedAt(), new UserDTO(service1.getUser().getId(), service1.getUser().getEmail(), service1.getUser().getCreatedAt())
                )).toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RestApiResponse<>(true, "get " + services.size() + " service(s)", services));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getService(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        try {
            Service service1 = this.service.getServiceById(id);
            if (!service1.getUser().getEmail().equals(email)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new RestApiResponse<>(false, "U don't have access to this service", null));
            }
            ServiceDTO dto = new ServiceDTO(
                    service1.getId(), service1.getName(), service1.getUrl(), service1.getStatus(),
                    service1.getLastStatusCode(), service1.getLastResponseTime(), service1.getLastCheckedAt(),
                    service1.getCreatedAt(), new UserDTO(service1.getUser().getId(), service1.getUser().getEmail(), service1.getUser().getCreatedAt())
            );
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new RestApiResponse<>(true, "Success", dto));
        } catch (IDNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new RestApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/check")
    public ResponseEntity<?> check(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        try {
            Check check = service.checkService(id);
            Service service1 = check.getService();
            if (!service1.getUser().getEmail().equals(email)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new RestApiResponse<>(false, "Unauthorized to do this operation", null));
            }
            CheckDTO dto = getCheckDTO(service1, check);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new RestApiResponse<>(true, "The service is checked", dto));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private static CheckDTO getCheckDTO(Service service1, Check check) {
        ServiceDTO serviceDto = new ServiceDTO(
               service1.getId(), service1.getName(), service1.getUrl(), service1.getStatus(),
               service1.getLastStatusCode(), service1.getLastResponseTime(), service1.getLastCheckedAt(),
               service1.getCreatedAt(), new UserDTO(service1.getUser().getId(), service1.getUser().getEmail(),
               service1.getUser().getCreatedAt())
       );
        return new CheckDTO(
                check.getId(), check.getStatus(), check.getStatusCode(),
                check.getResponseTime(), check.getCheckedAt(), serviceDto
        );
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getHistory(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        List<CheckDTO> history = service
                .getHistory(id)
                .stream()
                .filter(check -> check.getService().getUser().getEmail().equals(email))
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
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RestApiResponse<>(true, "Success", history));
    }
}
