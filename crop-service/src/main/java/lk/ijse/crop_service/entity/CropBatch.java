package lk.ijse.crop_service.entity;

import jakarta.persistence.*;

@Entity
public class CropBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cropName;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private CropStatus status;

    public CropBatch() {}

    public CropBatch(Long id, String cropName, Integer quantity, CropStatus status) {
        this.id = id;
        this.cropName = cropName;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCropName() {
        return cropName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CropStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setStatus(CropStatus status) {
        this.status = status;
    }
}