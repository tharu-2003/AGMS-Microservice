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

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final IoTClient ioTClient;

    private String cachedExternalToken = null;

    public ZoneService(ZoneRepository zoneRepository, IoTClient ioTClient) {
        this.zoneRepository = zoneRepository;
        this.ioTClient = ioTClient;
    }

    @Transactional
    public Zone create(ZoneRequest request, String localToken) {
        // 1. Validation
        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new RuntimeException("Min temperature must be less than Max temperature");
        }

        Zone zone = new Zone();
        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());

        Zone savedZone = zoneRepository.save(zone);

        String deviceId = null;

        try {
            // 2. Obtain Secure Session (External Login)
            if (cachedExternalToken == null) {
                Map<String, String> loginReq = Map.of(
                        "username", "Tharusha",
                        "password", "1234"
                );
                Map<String, Object> loginResponse = ioTClient.login(loginReq);
                cachedExternalToken = "Bearer " + loginResponse.get("accessToken").toString();
            }

            // 3. External Integration: Register Device
            DeviceCreateRequest deviceReq = new DeviceCreateRequest(
                    savedZone.getName() + "-sensor",
                    "ZONE-" + savedZone.getId()
            );

            DeviceCreateResponse deviceResponse = ioTClient.registerDevice(cachedExternalToken, deviceReq);
            deviceId = deviceResponse.getDeviceId();

        } catch (Exception e) {
            cachedExternalToken = null;
            System.err.println("IoT Integration Error: " + e.getMessage());
            deviceId = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        // 4. Update deviceId and Save
        savedZone.setDeviceId(deviceId);
        return zoneRepository.save(savedZone);
    }

    public Zone getById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + id));
    }

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

    public void delete(Long id) {
        if (!zoneRepository.existsById(id)) {
            throw new RuntimeException("Zone not found");
        }
        zoneRepository.deleteById(id);
    }
}