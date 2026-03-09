package lk.ijse.zone_service.dto;

public class ZoneRequest {

    private String name;
    private Double minTemp;
    private Double maxTemp;

    public ZoneRequest() {
    }

    public ZoneRequest(String name, Double minTemp, Double maxTemp) {
        this.name = name;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }
}