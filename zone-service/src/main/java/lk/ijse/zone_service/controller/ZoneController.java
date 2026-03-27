package lk.ijse.zone_service.controller;

import lk.ijse.zone_service.dto.ZoneRequest;
import lk.ijse.zone_service.entity.Zone;
import lk.ijse.zone_service.service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<Zone> create(@RequestBody ZoneRequest request) {
        return ResponseEntity.ok(zoneService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> get(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zone> update(@PathVariable Long id, @RequestBody ZoneRequest request) {
        return ResponseEntity.ok(zoneService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}