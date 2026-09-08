package com.mmed.ws.model;

import com.mmed.ws.util.MyUtil;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String url;
    private String status; // UP OR DOWN

    @Column(name = "last_status_code")
    private int lastStatusCode;

    @Column(name = "last_response_time")
    private int lastResponseTime;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Service(UUID id, String name, String url, String status, int lastStatusCode, int lastResponseTime, LocalDateTime lastCheckedAt, LocalDateTime createdAt, User user) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
        this.lastStatusCode = lastStatusCode;
        this.lastResponseTime = lastResponseTime;
        this.lastCheckedAt = lastCheckedAt;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Service(String name, String url, String status, int lastStatusCode, int lastResponseTime, LocalDateTime lastCheckedAt, LocalDateTime createdAt, User user) {
        this(
                MyUtil.generateId(), name, url, status, lastStatusCode,
                lastResponseTime, lastCheckedAt, createdAt, user
        );
    }

    public Service() {
        this.createdAt = LocalDateTime.now();
        this.status = "UP";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLastStatusCode() {
        return lastStatusCode;
    }

    public void setLastStatusCode(int lastStatusCode) {
        this.lastStatusCode = lastStatusCode;
    }

    public int getLastResponseTime() {
        return lastResponseTime;
    }

    public void setLastResponseTime(int lastResponseTime) {
        this.lastResponseTime = lastResponseTime;
    }

    public LocalDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(LocalDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
