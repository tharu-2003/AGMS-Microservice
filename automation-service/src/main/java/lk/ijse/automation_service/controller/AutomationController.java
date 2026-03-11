package lk.ijse.automation_service.controller;

import lk.ijse.automation_service.dto.TelemetryRequest;
import lk.ijse.automation_service.entity.AutomationLog;
import lk.ijse.automation_service.service.AutomationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    private final AutomationService automationService;

    public AutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> process(@RequestBody TelemetryRequest request) {
        return ResponseEntity.ok(automationService.process(request));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AutomationLog>> getLogs() {
        return ResponseEntity.ok(automationService.getLogs());
    }
}