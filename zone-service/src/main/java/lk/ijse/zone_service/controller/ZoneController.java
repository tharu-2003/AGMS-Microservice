package lk.ijse.zone_service.controller;

import lk.ijse.zone_service.dto.ZoneRequest;
import lk.ijse.zone_service.entity.Zone;
import lk.ijse.zone_service.service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles all web requests related to Greenhouse Zones.
 * It allows the farmer to define specific areas (e.g., Tomato Zone) and
 * set their ideal environmental limits.
 */
@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    /**
     * Endpoint to create a new zone.
     * This also triggers the registration of an IoT device for the zone.
     *
     * @param request Contains the zone name and temperature thresholds.
     * @return The newly created Zone object.
     */
    @PostMapping
    public ResponseEntity<Zone> create(@RequestBody ZoneRequest request) {
        // Calls the service layer to validate limits and register with the IoT API
        return ResponseEntity.ok(zoneService.create(request));
    }

    /**
     * Endpoint to fetch details of a specific zone using its ID.
     *
     * @param id The unique identifier of the zone.
     * @return Zone details including name, limits, and the associated device ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Zone> get(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getById(id));
    }

    /**
     * Endpoint to update the settings (like temperature limits) of an existing zone.
     *
     * @param id The ID of the zone to modify.
     * @param request The updated name and temperature thresholds.
     * @return The updated Zone object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Zone> update(@PathVariable Long id, @RequestBody ZoneRequest request) {
        return ResponseEntity.ok(zoneService.update(id, request));
    }

    /**
     * Endpoint to remove a zone from the system.
     *
     * @param id The unique identifier of the zone to be deleted.
     * @return A 204 No Content response upon successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}