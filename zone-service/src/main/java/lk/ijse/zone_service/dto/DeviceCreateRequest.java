package lk.ijse.zone_service.dto;

public class DeviceCreateRequest {

    private String name;
    private String zoneId;

    public DeviceCreateRequest() {
    }

    public DeviceCreateRequest(String name, String zoneId) {
        this.name = name;
        this.zoneId = zoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}