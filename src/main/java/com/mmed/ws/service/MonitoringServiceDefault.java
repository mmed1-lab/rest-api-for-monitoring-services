package com.mmed.ws.service;

import com.mmed.ws.exception.IDNotFoundException;
import com.mmed.ws.exception.UniqueUrlException;
import com.mmed.ws.model.Check;
import com.mmed.ws.model.Service;
import com.mmed.ws.repository.CheckRepository;
import com.mmed.ws.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class MonitoringServiceDefault implements MonitoringService {

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private HttpClient client;


    @Override
    public Service addService(Service service) {
        Service service1 = serviceRepository.findByUrl(service.getUrl()).orElse(null);
        if (service1 != null)
            throw new UniqueUrlException();
        return serviceRepository.save(service);
    }

    @Override
    public List<Service> getAllService(UUID userId) {
        return serviceRepository.findUserServices(userId);
    }

    @Override
    public Service getServiceById(UUID id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            throw new IDNotFoundException("No service with id equals to " + id);
        }
        return service;
    }

    @Override
    public Check checkService(UUID id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            throw new IDNotFoundException("No service with id equals to " + id);
        }

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(service.getUrl()))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        int statusCode;
        long responseTime;
        String status;

        Instant start = Instant.now();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseTime = Duration.between(start, Instant.now()).toMillis();
            statusCode = httpResponse.statusCode();
            status = (statusCode >= 200 && statusCode < 400) ? "UP" : "DOWN";
        } catch (IOException e) {
            // connection failed, timed out, DNS failure, etc — service is unreachable
            responseTime = Duration.between(start, Instant.now()).toMillis();
            statusCode = 0;
            status = "DOWN";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupt flag, don't swallow it
            throw new RuntimeException("Health check interrupted", e);
        }

        Check check = new Check();
        check.setStatus(status);
        check.setStatusCode(statusCode);
        check.setResponseTime((int) responseTime);
        check.setCheckedAt(LocalDateTime.now());
        check.setService(service);

        return checkRepository.save(check);
    }

}
