package lk.ijse.automation_service.service;

import lk.ijse.automation_service.client.ZoneClient;
import lk.ijse.automation_service.dto.TelemetryRequest;
import lk.ijse.automation_service.dto.ZoneResponse;
import lk.ijse.automation_service.entity.AutomationLog;
import lk.ijse.automation_service.repository.AutomationLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutomationService {

    private final AutomationLogRepository repository;
    private final ZoneClient zoneClient;

    public AutomationService(AutomationLogRepository repository, ZoneClient zoneClient) {
        this.repository = repository;
        this.zoneClient = zoneClient;
    }

    public String process(TelemetryRequest request) {
        Long zoneId = Long.parseLong(request.getZoneId());
        ZoneResponse zone = zoneClient.getZone(zoneId);

        String action = "NO_ACTION";

        if (request.getTemperature() > zone.getMaxTemp()) {
            action = "TURN_FAN_ON";
        } else if (request.getTemperature() < zone.getMinTemp()) {
            action = "TURN_HEATER_ON";
        }

        AutomationLog log = new AutomationLog();
        log.setZoneId(request.getZoneId());
        log.setTemperature(request.getTemperature());
        log.setAction(action);
        log.setCreatedAt(LocalDateTime.now().toString());

        repository.save(log);
        return action;
    }

    public List<AutomationLog> getLogs() {
        return repository.findAll();
    }
}