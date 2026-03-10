package lk.ijse.crop_service.dto;

public class CropRequest {

    private String cropName;
    private Integer quantity;

    public CropRequest() {}

    public String getCropName() {
        return cropName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}