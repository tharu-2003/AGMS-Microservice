package lk.ijse.sensor_service.dto;

public class TelemetryRequest {

    private String zoneId;
    private Double temperature;
    private Double humidity;

    public TelemetryRequest() {
    }

    public String getZoneId() {
        return zoneId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
}