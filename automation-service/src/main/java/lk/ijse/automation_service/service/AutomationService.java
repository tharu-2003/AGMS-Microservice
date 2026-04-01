package lk.ijse.automation_service.service;

import lk.ijse.automation_service.client.ZoneClient;
import lk.ijse.automation_service.dto.TelemetryRequest;
import lk.ijse.automation_service.dto.ZoneResponse;
import lk.ijse.automation_service.entity.AutomationLog;
import lk.ijse.automation_service.repository.AutomationLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This service is the "Brain" of the system.
 * It evaluates sensor data against set thresholds and decides which
 * climate control hardware (Fans or Heaters) should be activated.
 */
@Service
public class AutomationService {

    private final AutomationLogRepository repository;
    private final ZoneClient zoneClient;

    public AutomationService(AutomationLogRepository repository, ZoneClient zoneClient) {
        this.repository = repository;
        this.zoneClient = zoneClient;
    }

    /**
     * Processes incoming telemetry data and executes automation rules.
     *
     * @param request The current temperature and humidity from the sensor.
     * @return The action string (e.g., "TURN_FAN_ON").
     */
    public String process(TelemetryRequest request) {
        // 1. Convert the zoneId string to a Long to find the zone in our system
        Long zoneId = Long.parseLong(request.getZoneId());

        // 2. Fetch the specific min/max temperature limits for this zone
        // by calling the Zone Service through OpenFeign
        ZoneResponse zone = zoneClient.getZone(zoneId);

        // 3. Initialize the default action
        String action = "NO_ACTION";

        // 4. RULE ENGINE LOGIC:
        // If current temperature is higher than the allowed maximum, turn the fan on.
        if (request.getTemperature() > zone.getMaxTemp()) {
            action = "TURN_FAN_ON";
        }
        // If current temperature is lower than the allowed minimum, turn the heater on.
        else if (request.getTemperature() < zone.getMinTemp()) {
            action = "TURN_HEATER_ON";
        }

        // 5. DATA LOGGING:
        // Create a history record of this decision for the farmer's records.
        AutomationLog log = new AutomationLog();
        log.setZoneId(request.getZoneId());
        log.setTemperature(request.getTemperature());
        log.setAction(action);
        log.setCreatedAt(LocalDateTime.now().toString());

        // 6. Save the log to the database
        repository.save(log);

        // 7. Return the final decision back to the caller (Sensor Service)
        return action;
    }

    /**
     * Retrieves the entire history of automated actions.
     */
    public List<AutomationLog> getLogs() {
        return repository.findAll();
    }
}