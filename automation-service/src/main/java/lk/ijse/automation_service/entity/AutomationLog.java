package lk.ijse.automation_service.entity;

import jakarta.persistence.*;

@Entity
public class AutomationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneId;
    private Double temperature;
    private String action;
    private String createdAt;

    public AutomationLog() {}

    public AutomationLog(Long id, String zoneId, Double temperature, String action, String createdAt) {
        this.id = id;
        this.zoneId = zoneId;
        this.temperature = temperature;
        this.action = action;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getZoneId() { return zoneId; }
    public Double getTemperature() { return temperature; }
    public String getAction() { return action; }
    public String getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public void setAction(String action) { this.action = action; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}