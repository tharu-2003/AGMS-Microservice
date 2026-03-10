package lk.ijse.crop_service.repository;

import lk.ijse.crop_service.entity.CropBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropBatchRepository extends JpaRepository<CropBatch, Long> {
}