package lk.ijse.crop_service.service;

import lk.ijse.crop_service.dto.CropRequest;
import lk.ijse.crop_service.entity.CropBatch;
import lk.ijse.crop_service.entity.CropStatus;
import lk.ijse.crop_service.repository.CropBatchRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CropService {

    private final CropBatchRepository repository;

    public CropService(CropBatchRepository repository) {
        this.repository = repository;
    }

    public CropBatch create(CropRequest request) {
        CropBatch batch = new CropBatch();
        batch.setCropName(request.getCropName());
        batch.setQuantity(request.getQuantity());
        batch.setStatus(CropStatus.SEEDLING); // Default status
        // වගා කළ වේලාව සටහන් කිරීම හොඳ පුරුද්දක්
        return repository.save(batch);
    }

    public CropBatch updateStatus(Long id, CropStatus nextStatus) {
        CropBatch batch = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop batch not found with ID: " + id));

        CropStatus currentStatus = batch.getStatus();

        // --- State Machine Logic ---
        if (currentStatus == CropStatus.HARVESTED) {
            throw new RuntimeException("Cannot change status. This batch is already HARVESTED.");
        }

        if (currentStatus == CropStatus.SEEDLING && nextStatus != CropStatus.VEGETATIVE) {
            throw new RuntimeException("Invalid transition. SEEDLING can only move to VEGETATIVE.");
        }

        if (currentStatus == CropStatus.VEGETATIVE && nextStatus != CropStatus.HARVESTED) {
            throw new RuntimeException("Invalid transition. VEGETATIVE can only move to HARVESTED.");
        }

        batch.setStatus(nextStatus);
        return repository.save(batch);
    }

    public List<CropBatch> getAll() {
        return repository.findAll();
    }
}