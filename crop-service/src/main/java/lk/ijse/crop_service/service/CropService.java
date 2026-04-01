package lk.ijse.crop_service.service;

import lk.ijse.crop_service.dto.CropRequest;
import lk.ijse.crop_service.entity.CropBatch;
import lk.ijse.crop_service.entity.CropStatus;
import lk.ijse.crop_service.repository.CropBatchRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This service manages the lifecycle of crops in the greenhouse.
 * It uses "State Machine" logic to ensure that plants grow in the correct order.
 */
@Service
public class CropService {

    private final CropBatchRepository repository;

    public CropService(CropBatchRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new crop record in the system.
     * Every new batch starts at the 'SEEDLING' stage by default.
     */
    public CropBatch create(CropRequest request) {
        CropBatch batch = new CropBatch();
        batch.setCropName(request.getCropName());
        batch.setQuantity(request.getQuantity());

        // Initial state of the plant
        batch.setStatus(CropStatus.SEEDLING);

        return repository.save(batch);
    }

    /**
     * Updates the growth stage of a crop batch.
     * This method contains the State Machine logic to prevent illegal status changes.
     */
    public CropBatch updateStatus(Long id, CropStatus nextStatus) {
        // 1. Check if the batch exists in the database
        CropBatch batch = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop batch not found with ID: " + id));

        CropStatus currentStatus = batch.getStatus();

        // --- STATE MACHINE LOGIC (LIFECYCLE RULES) ---

        // Rule 1: Once a crop is HARVESTED, its status can never be changed again.
        if (currentStatus == CropStatus.HARVESTED) {
            throw new RuntimeException("Process Blocked: This batch is already HARVESTED.");
        }

        // Rule 2: A SEEDLING must become VEGETATIVE before it can be harvested.
        if (currentStatus == CropStatus.SEEDLING && nextStatus != CropStatus.VEGETATIVE) {
            throw new RuntimeException("Invalid Step: SEEDLING can only move to the VEGETATIVE stage.");
        }

        // Rule 3: A VEGETATIVE plant can only move to the final HARVESTED stage.
        if (currentStatus == CropStatus.VEGETATIVE && nextStatus != CropStatus.HARVESTED) {
            throw new RuntimeException("Invalid Step: VEGETATIVE plants can only move to the HARVESTED stage.");
        }

        // 2. If the transition is valid, update the status and save
        batch.setStatus(nextStatus);
        return repository.save(batch);
    }

    /**
     * Returns a list of all crop batches in the greenhouse.
     */
    public List<CropBatch> getAll() {
        return repository.findAll();
    }
}