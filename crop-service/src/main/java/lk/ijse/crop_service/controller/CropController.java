package lk.ijse.crop_service.controller;

import lk.ijse.crop_service.dto.CropRequest;
import lk.ijse.crop_service.entity.CropBatch;
import lk.ijse.crop_service.entity.CropStatus;
import lk.ijse.crop_service.service.CropService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService service;

    public CropController(CropService service) {
        this.service = service;
    }

    @PostMapping
    public CropBatch create(@RequestBody CropRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<CropBatch> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}/status")
    public CropBatch updateStatus(
            @PathVariable Long id,
            @RequestParam CropStatus status
    ) {
        return service.updateStatus(id, status);
    }
}