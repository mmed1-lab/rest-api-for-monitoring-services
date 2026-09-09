package com.mmed.ws.service;

import com.mmed.ws.model.Check;
import com.mmed.ws.model.Service;

import java.util.List;
import java.util.UUID;

public interface MonitoringService {

    Service addService(Service service);
    List<Service> getAllService(UUID userId);
    Service getServiceById(UUID id);
    Check checkService(UUID id);
    List<Check> getHistory(UUID serviceId);

}
