package lk.ijse.sensor_service.client;

import lk.ijse.sensor_service.dto.TelemetryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This interface acts as the "Pusher" tool.
 * It allows the Sensor Service to send data directly to the Automation Service
 * using Spring Cloud OpenFeign.
 */
@FeignClient(name = "AUTOMATION-SERVICE")
public interface AutomationClient {

    /**
     * This method sends the collected temperature and humidity data to the
     * Automation Service's processing endpoint.
     *
     * @param request Contains the current sensor values and the zone ID.
     * @return The result of the automation logic (e.g., "TURN_FAN_ON" or "NO_ACTION").
     */
    @PostMapping("/api/automation/process")
    String pushToAutomation(@RequestBody TelemetryRequest request);
}