package lk.ijse.automation_service.controller;

import lk.ijse.automation_service.dto.TelemetryRequest;
import lk.ijse.automation_service.entity.AutomationLog;
import lk.ijse.automation_service.service.AutomationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller acts as the entry point for the system's "Brain".
 * It handles incoming sensor data and provides a history of automated actions.
 */
@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    private final AutomationService automationService;

    public AutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }

    /**
     * This is an internal endpoint called by the Sensor Service.
     * It receives real-time temperature and humidity data, processes it through
     * the rule engine, and decides what action to take (e.g., Turn on Fan).
     *
     * @param request Contains current temperature and zone information.
     * @return A message indicating the action taken (e.g., "TURN_FAN_ON").
     */
    @PostMapping("/process")
    public ResponseEntity<String> process(@RequestBody TelemetryRequest request) {
        // Sends data to the service layer to make a decision
        return ResponseEntity.ok(automationService.process(request));
    }

    /**
     * This endpoint allows the farmer to view a history of all automated
     * actions taken by the system.
     *
     * @return A list of automation logs from the database.
     */
    @GetMapping("/logs")
    public ResponseEntity<List<AutomationLog>> getLogs() {
        // Fetches all saved actions for the farmer to see
        return ResponseEntity.ok(automationService.getLogs());
    }
}