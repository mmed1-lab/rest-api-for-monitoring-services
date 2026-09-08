package com.mmed.ws.controller;

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

    @GetMapping(params = {"u"})
    public ResponseEntity<?> getServices(@RequestParam("u") UUID id) {
        List<ServiceDTO> services = service
                .getAllService(id).stream()
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
    public ResponseEntity<?> getService(@PathVariable UUID id) {
        try {
            Service service1 = this.service.getServiceById(id);
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
    public ResponseEntity<?> check(@PathVariable UUID id) {
        try {
            Check check = service.checkService(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new RestApiResponse<>(true, "The service is checked", check));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
