package lk.ijse.automation_service.dto;

public class ZoneResponse {

    private Long id;
    private String name;
    private Double minTemp;
    private Double maxTemp;
    private String deviceId;

    public ZoneResponse() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getMinTemp() { return minTemp; }
    public Double getMaxTemp() { return maxTemp; }
    public String getDeviceId() { return deviceId; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMinTemp(Double minTemp) { this.minTemp = minTemp; }
    public void setMaxTemp(Double maxTemp) { this.maxTemp = maxTemp; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
}