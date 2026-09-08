package com.mmed.ws.model;

import com.mmed.ws.util.MyUtil;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "checks")
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String status; // UP or DOWN

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "response_time")
    private int responseTime;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    public Check(UUID id, String status, int statusCode, int responseTime, LocalDateTime checkedAt, Service service) {
        this.id = id;
        this.status = status;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
        this.checkedAt = checkedAt;
        this.service = service;
    }

    public Check(String status, int statusCode, int responseTime, LocalDateTime checkedAt, Service service) {
        this(MyUtil.generateId(), status, statusCode, responseTime, checkedAt, service);
    }

    public Check() {
        this.checkedAt = LocalDateTime.now();
        this.status = "UP";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
