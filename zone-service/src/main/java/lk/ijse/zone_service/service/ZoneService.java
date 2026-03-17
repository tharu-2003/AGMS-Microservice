package lk.ijse.zone_service.service;

import lk.ijse.zone_service.client.IoTClient;
import lk.ijse.zone_service.dto.DeviceCreateRequest;
import lk.ijse.zone_service.dto.DeviceCreateResponse;
import lk.ijse.zone_service.dto.ZoneRequest;
import lk.ijse.zone_service.entity.Zone;
import lk.ijse.zone_service.repository.ZoneRepository;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final IoTClient ioTClient;

    public ZoneService(ZoneRepository zoneRepository, IoTClient ioTClient) {
        this.zoneRepository = zoneRepository;
        this.ioTClient = ioTClient;
    }

    public Zone create(ZoneRequest request, String accessToken) {
        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new RuntimeException("minTemp must be less than maxTemp");
        }

        Zone zone = new Zone();
        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());

        Zone savedZone = zoneRepository.save(zone);

//        DeviceCreateRequest deviceRequest = new DeviceCreateRequest(
//                savedZone.getName() + "-sensor",
//                "ZONE-" + savedZone.getId()
//        );
//
//        DeviceCreateResponse deviceResponse = ioTClient.registerDevice(accessToken, deviceRequest);
//
//        savedZone.setDeviceId(deviceResponse.getDeviceId());

        // Temporary test without external IoT API call
        savedZone.setDeviceId("TEMP-DEVICE-ID");

        return zoneRepository.save(savedZone);
    }

//    For Testing without JWT Token
//    public Zone create(ZoneRequest request) {
//        if (request.getMinTemp() >= request.getMaxTemp()) {
//            throw new RuntimeException("minTemp must be less than maxTemp");
//        }
//
//        Zone zone = new Zone();
//        zone.setName(request.getName());
//        zone.setMinTemp(request.getMinTemp());
//        zone.setMaxTemp(request.getMaxTemp());
//        zone.setDeviceId("TEMP-DEVICE-ID");
//
//        return zoneRepository.save(zone);
//    }

    public Zone getById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    public Zone update(Long id, ZoneRequest request) {
        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new RuntimeException("minTemp must be less than maxTemp");
        }

        Zone zone = getById(id);
        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());

        return zoneRepository.save(zone);
    }

    public void delete(Long id) {
        zoneRepository.deleteById(id);
    }
}