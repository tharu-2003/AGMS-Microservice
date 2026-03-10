package lk.ijse.crop_service.service;

import lk.ijse.crop_service.dto.CropRequest;
import lk.ijse.crop_service.entity.CropBatch;
import lk.ijse.crop_service.entity.CropStatus;
import lk.ijse.crop_service.repository.CropBatchRepository;
import org.springframework.stereotype.Service;

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
        batch.setStatus(CropStatus.SEEDLING);

        return repository.save(batch);
    }

    public List<CropBatch> getAll() {
        return repository.findAll();
    }

    public CropBatch updateStatus(Long id, CropStatus status) {

        CropBatch batch = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop batch not found"));

        batch.setStatus(status);

        return repository.save(batch);
    }
}