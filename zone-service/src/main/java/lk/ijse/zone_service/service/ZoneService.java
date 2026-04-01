package lk.ijse.zone_service.service;

import jakarta.transaction.Transactional;
import lk.ijse.zone_service.client.IoTClient;
import lk.ijse.zone_service.dto.DeviceCreateRequest;
import lk.ijse.zone_service.dto.DeviceCreateResponse;
import lk.ijse.zone_service.dto.ZoneRequest;
import lk.ijse.zone_service.entity.Zone;
import lk.ijse.zone_service.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * This service handles the core business logic for managing greenhouse zones.
 * It manages temperature thresholds and coordinates with an external IoT provider
 * to register hardware sensors for each zone.
 */
@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final IoTClient ioTClient;

    // Stores the external API token temporarily to avoid unnecessary logins
    private String cachedExternalToken = null;

    public ZoneService(ZoneRepository zoneRepository, IoTClient ioTClient) {
        this.zoneRepository = zoneRepository;
        this.ioTClient = ioTClient;
    }

    /**
     * Creates a new greenhouse zone and links it to an IoT device.
     */
    @Transactional
    public Zone create(ZoneRequest request) {
        // 1. BUSINESS RULE VALIDATION
        // Enforce the rule that the minimum temperature cannot be higher than the maximum.
        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new RuntimeException("Invalid Temperature: Minimum temperature must be less than Maximum temperature.");
        }

        // 2. INITIAL PERSISTENCE
        // Create and save the basic zone info first to generate a unique database ID.
        Zone zone = new Zone();
        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());

        Zone savedZone = zoneRepository.save(zone);

        String deviceId = null;

        try {
            // 3. EXTERNAL AUTHENTICATION
            // If we don't have a valid session with the external IoT API, we log in now.
            if (cachedExternalToken == null) {
                Map<String, String> loginReq = Map.of(
                        "username", "Tharusha", // Project specific credentials
                        "password", "1234"
                );
                Map<String, Object> loginResponse = ioTClient.login(loginReq);
                cachedExternalToken = "Bearer " + loginResponse.get("accessToken").toString();
            }

            // 4. EXTERNAL IOT REGISTRATION
            // Register a virtual hardware sensor in the external system for this specific zone.
            DeviceCreateRequest deviceReq = new DeviceCreateRequest(
                    savedZone.getName() + "-sensor",
                    "ZONE-" + savedZone.getId()
            );

            DeviceCreateResponse deviceResponse = ioTClient.registerDevice(cachedExternalToken, deviceReq);
            deviceId = deviceResponse.getDeviceId();

        } catch (Exception e) {
            // 5. RESILIENCE (FALLBACK LOGIC)
            // If the external API is unreachable, generate a simulated ID.
            // This ensures the system remains functional for demonstration and testing.
            cachedExternalToken = null;
            System.err.println("IoT API Offline: Generating a simulated ID instead.");
            deviceId = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        // 6. FINAL UPDATE
        // Link the resulting deviceId (real or simulated) to the zone and update the database.
        savedZone.setDeviceId(deviceId);
        return zoneRepository.save(savedZone);
    }

    /**
     * Fetches a specific zone by its database ID.
     */
    public Zone getById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + id));
    }

    /**
     * Updates an existing zone's information.
     * Also re-validates the temperature limits before saving.
     */
    @Transactional
    public Zone update(Long id, ZoneRequest request) {
        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new RuntimeException("Validation Failed: minTemp must be less than maxTemp");
        }

        Zone zone = getById(id);
        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());

        return zoneRepository.save(zone);
    }

    /**
     * Removes a zone from the system.
     */
    public void delete(Long id) {
        if (!zoneRepository.existsById(id)) {
            throw new RuntimeException("Zone not found");
        }
        zoneRepository.deleteById(id);
    }
}