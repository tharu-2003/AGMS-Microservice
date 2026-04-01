package lk.ijse.crop_service.controller;

import lk.ijse.crop_service.dto.CropRequest;
import lk.ijse.crop_service.entity.CropBatch;
import lk.ijse.crop_service.entity.CropStatus;
import lk.ijse.crop_service.service.CropService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles all requests related to crop inventory management.
 * It allows the farmer to register new crop batches and track their growth stages.
 */
@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService service;

    public CropController(CropService service) {
        this.service = service;
    }

    /**
     * Endpoint to register a new batch of crops in the greenhouse.
     * By default, a new batch starts at the 'SEEDLING' stage.
     *
     * @param request Contains crop name and quantity.
     * @return The newly created CropBatch object.
     */
    @PostMapping
    public CropBatch create(@RequestBody CropRequest request) {
        return service.create(request);
    }

    /**
     * Endpoint to view all crop batches currently in the system.
     * This helps the farmer monitor the entire greenhouse inventory.
     *
     * @return A list of all crop batches.
     */
    @GetMapping
    public List<CropBatch> getAll() {
        return service.getAll();
    }

    /**
     * Endpoint to update the growth stage of a specific crop batch.
     * This triggers the "State Machine" logic to ensure stages follow the correct order
     * (e.g., Seedling -> Vegetative -> Harvested).
     *
     * @param id The ID of the crop batch to update.
     * @param status The next stage in the plant's lifecycle.
     * @return The updated CropBatch object.
     */
    @PutMapping("/{id}/status")
    public CropBatch updateStatus(
            @PathVariable Long id,
            @RequestParam CropStatus status
    ) {
        return service.updateStatus(id, status);
    }
}